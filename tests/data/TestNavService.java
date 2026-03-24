package data;

import model.RouteStep;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TestNavService implements NavigationService {

    private final Map<String, List<RouteStep>> testPaths = new HashMap<>();

    public TestNavService() {

        testPaths.put("SCI_101->SCI_105", List.of(
                new RouteStep("Walk down the North hallway", "WAY_HALL_1"),
                new RouteStep("Arrive at Room 105 on your right", "SCI_105")
        ));


        testPaths.put("SCI_101->SCI_202", List.of(
                new RouteStep("Walk to Stairwell A", "WAY_STAIR_A1"),
                new RouteStep("Take stairs to Floor 2", "WAY_STAIR_A2"),
                new RouteStep("Arrive at Room 202", "SCI_202")
        ));

        testPaths.put("SCI_101->BIT_101", List.of(
                new RouteStep("Exit Science Building South Door", "WAY_SCI_EXIT"),
                new RouteStep("Cross the courtyard to Business Building", "WAY_BIT_ENTRANCE"),
                new RouteStep("Arrive at BIT 101", "BIT_101")
        ));
    }

    @Override
    public List<RouteStep> getDirections(String startRoom, String endRoom) {
//        if (startRoom.equals(endRoom)) {
            return new ArrayList<>();
//        }
//        String key = startRoom + "->" + endRoom;
//        return testPaths.getOrDefault(key, new ArrayList<>());
    }
}