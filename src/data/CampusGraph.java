package data;

import model.RouteStep;
import model.Waypoint;

import java.io.*;
import java.util.*;
public class CampusGraph {

    private static final class Flag {
        final String area;
        final int weight = 1;

        Flag(String area) {
            this.area = area;
        }
    }

    private final Map<String, Waypoint>    nodes   = new HashMap<>();
    private final Map<String, Flag>  adjList = new HashMap<>();

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
                if (line.isEmpty()){
                    continue;
                }

                String[] parts = line.split(",");
                String id = parts[0].trim();
                nodes.put(id, new Waypoint(id));

                String flagString = parts[3].trim();
                Flag newFlag = new Flag(flagString);
                adjList.put(id, newFlag);
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

            for (Flag flag : adjList.getOrDefault(current, null)) {
                if (!nodes.containsKey(flag.area)) continue;

                int candidate = currentDist + flag.weight;
                if (candidate < dist.getOrDefault(flag.area, Integer.MAX_VALUE)) {
                    dist.put(flag.area, candidate);
                    prev.put(flag.area, current);
                    pq.add(flag.area);
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
            boolean  isLast = (i == path.size() - 1);

            String instruction;

            if (!prev.getBuilding().equals(curr.getBuilding())) {
                instruction = "Exit " + friendlyBuilding(prev.getBuilding())
                        + " and head towards " + curr.getFloor() + curr.getBuilding();

            } else if (prev.getFloor() != curr.getFloor()) {
                String direction = curr.getFloor() > prev.getFloor() ? "up" : "down";
                instruction = "Take the stairs or elevator " + direction
                        + " to floor " + curr.getFloor()
                        + ": " + curr.getFloor() + curr.getBuilding();

            } else if (isLast) {
                instruction = "Arrive at " + curr.getFloor() + curr.getBuilding();

            } else {
                instruction = "Continue along the hallway to " + curr.getFloor() + curr.getBuilding();
            }

            steps.add(new RouteStep(instruction, curr.getId()));
        }

        return steps;
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
