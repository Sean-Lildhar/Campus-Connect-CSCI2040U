package data;

import model.RouteStep;
import java.util.List;

public class DijkstraNavigationService implements NavigationService {

    private final CampusGraph graph;
    public DijkstraNavigationService() {
        this.graph = new CampusGraph("data/locations.csv");
    }
    public DijkstraNavigationService(String graphPath) {
        this.graph = new CampusGraph(graphPath);
    }

    @Override
    public List<RouteStep> getDirections(String startRoom, String endRoom) {
        return graph.dijkstra(startRoom, endRoom);
    }
}
