package data;

import model.RouteStep;
import model.Waypoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CampusGraph {

    private final Map<String, Waypoint> nodes = new HashMap<>();
    private final Map<String, List<Edge>> adjList = new HashMap<>();
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

    public List<RouteStep> dijkstra(String startId, String endId) {
        if (!nodes.containsKey(startId) || !nodes.containsKey(endId)) {
            return Collections.emptyList();
        }
        if (startId.equals(endId)) {
            return Collections.emptyList();
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

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

        return buildInstructions(reconstructPath(prev, endId));
    }

    private List<String> reconstructPath(Map<String, String> prev, String endId) {
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
            steps.add(new RouteStep(instruction, curr.id()));
        }

        return steps;
    }

    private String generateInstruction(Waypoint prev, Waypoint curr, boolean isLast) {

        String prevType = prev.locationType().toLowerCase();
        String currType = curr.locationType().toLowerCase();

        boolean prevIsHall = prevType.equals("hallway");
        boolean currIsHall = currType.equals("hallway");

        String prevName = formatName(prev);
        String currName = formatName(curr);

        //Building / Outdoor transition
        if (!prev.building().equals(curr.building())) {
            if (curr.id().equals("Polonsky_Commons")) {
                if (prev.id().startsWith("SIR")) {
                    return "Exit SIRC and cross Conlin Road to the main campus.";
                } else {
                    return "Exit the building into Polonsky Commons.";
                }
            } else if (prev.id().equals("Polonsky_Commons")) {
                if (curr.id().startsWith("SIR")) {
                    return "Cross Conlin Road and enter SIRC.";
                } else {
                    return "Enter " + currName + " from the Polonsky Commons.";
                }
            }
            return "Exit " + prevName + " and head to " + currName;
        }

        //Floor transition
        if (prev.floor() != curr.floor()) {
            String direction = curr.floor() > prev.floor() ? "up" : "down";
            String type = curr.locationType().toLowerCase();

            if (type.contains("stair")) {
                return "Take the stairs " + direction + " to floor " + curr.floor();
            } else if (type.contains("elevator")) {
                return "Take the elevator " + direction + " to floor " + curr.floor();
            } else {
                return "Go " + direction + " to floor " + curr.floor();
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
        if (prevIsHall) {
            return "Enter " + currName;
        }

        //Room to Room
        return "Proceed to " + currName;
    }

    private String formatName(Waypoint wp) {

        String type = wp.locationType().toLowerCase();
        String id = wp.id();

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
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    private record Edge(String neighborId, int weight) {
    }
}