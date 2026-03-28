package data;

import model.RouteStep;
import model.Waypoint;

import java.io.*;
import java.util.*;
public class CampusGraph {

    private static final class Edge {
        final String neighborId;
        final int weight;

        Edge(String neighborId, int weight) {
            this.neighborId = neighborId;
            this.weight = weight;
        }
    }

    private final Map<String, Waypoint>       nodes   = new HashMap<>();
    private final Map<String, List<Edge>>     adjList = new HashMap<>();

    public CampusGraph(String filePath) {
        load(filePath);
    }

    private void load(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                // Parse node info: nodeId,description,building,floor
                String id = parts[0].trim();
                String locationType = parts[1].trim();
                String description = "Room " + id;
                String building = id.substring(0, 3);
                int floor;
                try {
                    floor = Integer.parseInt(parts[3].trim());
                } catch (NumberFormatException e) {
                    floor = 0;
                }

                nodes.put(id, new Waypoint(id, description, building, floor, locationType));

                // Parse neighbors: neighbour1,weight1,neighbour2,weight2,...
                List<Edge> edges = new ArrayList<>();
                for (int i = 4; i < parts.length; i += 2) {
                    if (i + 1 < parts.length) {
                        String neighborId = parts[i].trim();
                        int weight;
                        try {
                            weight = Integer.parseInt(parts[i + 1].trim());
                        } catch (NumberFormatException e) {
                            weight = 1;
                        }
                        edges.add(new Edge(neighborId, weight));
                    }
                }
                adjList.put(id, edges);
            }
        } catch (IOException e) {
            System.err.println("CampusGraph.load error: " + e.getMessage());
        }
    }

    public boolean containsNode(String id) {
        return nodes.containsKey(id);
    }

    public List<RouteStep> dijkstra(String startId, String endId) {
        if (!nodes.containsKey(startId) || !nodes.containsKey(endId)) {
            return Collections.emptyList();
        }
        if (startId.equals(endId)) {
            return Collections.emptyList();
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String>  prev = new HashMap<>();

        for (String id : nodes.keySet()) dist.put(id, Integer.MAX_VALUE);
        dist.put(startId, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(
                Comparator.comparingInt(id -> dist.getOrDefault(id, Integer.MAX_VALUE)));
        pq.add(startId);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(endId)) break;

            int currentDist = dist.get(current);
            if (currentDist == Integer.MAX_VALUE) break;

            List<Edge> neighbors = adjList.getOrDefault(current, Collections.emptyList());
            for (Edge edge : neighbors) {
                if (!nodes.containsKey(edge.neighborId)) continue;

                int candidate = currentDist + edge.weight;
                if (candidate < dist.getOrDefault(edge.neighborId, Integer.MAX_VALUE)) {
                    dist.put(edge.neighborId, candidate);
                    prev.put(edge.neighborId, current);
                    pq.add(edge.neighborId);
                }
            }
        }

        if (dist.getOrDefault(endId, Integer.MAX_VALUE) == Integer.MAX_VALUE) {
            return Collections.emptyList();
        }

        return buildInstructions(reconstructPath(prev, startId, endId));
    }

    private List<String> reconstructPath(Map<String, String> prev,
                                         String startId, String endId) {
        LinkedList<String> path = new LinkedList<>();
        for (String at = endId; at != null; at = prev.get(at)) {
            path.addFirst(at);
        }
        return path;
    }

    private List<RouteStep> buildInstructions(List<String> path) {
        List<RouteStep> steps = new ArrayList<>();

        for (int i = 1; i < path.size(); i++) {
            Waypoint prev = nodes.get(path.get(i - 1));
            Waypoint curr = nodes.get(path.get(i));
            boolean isLast = (i == path.size() - 1);

            String instruction = generateInstruction(prev, curr, isLast);
            steps.add(new RouteStep(instruction, curr.getId()));
        }

        return steps;
    }

    private String generateInstruction(Waypoint prev, Waypoint curr, boolean isLast) {

        String prevType = prev.getLocationType().toLowerCase();
        String currType = curr.getLocationType().toLowerCase();

        boolean prevIsHall = prevType.equals("hallway");
        boolean currIsHall = currType.equals("hallway");

        String prevName = formatName(prev);
        String currName = formatName(curr);

        //Building transition
        if (!prev.getBuilding().equals(curr.getBuilding())) {
            return "Exit " + prevName + " and head to " + currName;
        }

        //Floor transition
        if (prev.getFloor() != curr.getFloor()) {
            String direction = curr.getFloor() > prev.getFloor() ? "up" : "down";

            String type = curr.getLocationType().toLowerCase();

            if (type.contains("stair")) {
                return "Take the stairs " + direction + " to floor " + curr.getFloor();
            } else if (type.contains("elevator")) {
                return "Take the elevator " + direction + " to floor " + curr.getFloor();
            } else {
                return "Go " + direction + " to floor " + curr.getFloor();
            }
        }

        //Final destination
        if (isLast) {
            return "Arrive at " + currName;
        }

        //Room to Hallway
        if (!prevIsHall && currIsHall) {
            return "Exit " + prevName + " into the " + currName;
        }

        //Hallway to Hallway
        if (prevIsHall && currIsHall) {
            return "Continue through the " + currName;
        }

        //Hallway to Room
        if (prevIsHall && !currIsHall) {
            return "Enter " + currName;
        }

        //Room → Room
        return "Proceed to " + currName;
    }

    private String formatName(Waypoint wp) {

        String type = wp.getLocationType().toLowerCase();
        String id = wp.getId();

        //Room
        if (type.equals("classroom")) {
            return "Room " + id;
        }

        //Hallway
        if (type.equals("hallway")) {
            return cleanName(id);
        }

        //Entrance / Exit
        if (type.contains("entrance") || type.contains("exit")) {
            return cleanName(id);
        }

        //Stairs
        if (type.contains("stair")) {
            return "stairs";
        }

        //Elevator
        if (type.contains("elevator")) {
            return "elevator";
        }

        //Default
        return cleanName(id);
    }

    private String cleanName(String id) {

        //Remove floor suffixes like F1, F2
        id = id.replaceAll("F\\d+", "");

        //Replace underscores
        id = id.replace("_", " ");

        //Trim extra spaces
        id = id.trim();

        //Capitalize words
        String[] words = id.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    private String friendlyBuilding(String code) {
        switch (code) {
            case "SCI":     return "the Science Building";
            case "BIT":     return "the Business Building";
            case "SHA":     return "Shawenjigewining Hall";
            case "SIR":     return "the Software Informatics and Research Centre";
            case "OUTDOOR": return "the campus path";
            default:        return code;
        }
    }
}