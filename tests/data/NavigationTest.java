package data;

import model.RouteStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class NavigationTest {

    private NavigationService navService;

    @BeforeEach
    void setUp() {
        //Replace with this later: navService = new BFSNavigationService();
        navService = new TestNavService();
    }

    @Test
    void testSameRoomNavigation() {
        List<RouteStep> path = navService.getDirections("SCI_101", "SCI_101");
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Path to same room should be empty or a 'stay put' instruction");
    }

    @Test
    void testSameFloorNavigation() {
        List<RouteStep> path = navService.getDirections("SCI_101", "SCI_105");
        assertFalse(path.isEmpty());
        assertTrue(path.get(0).getInstruction().contains("hallway"));
    }

    @Test
    void testCrossFloorNavigation() {
        List<RouteStep> path = navService.getDirections("SCI_101", "SCI_202");

        boolean usesStairs = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("stairs")
                        || step.getInstruction().toLowerCase().contains("elevator"));

        assertTrue(usesStairs, "Different floors path must mention changing floors");
    }

    @Test
    void testCrossBuildingNavigation() {
        List<RouteStep> path = navService.getDirections("SCI_101", "BIT_101");

        boolean exitsBuilding = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("exit"));

        assertTrue(exitsBuilding, "Different building path must mention changing buildings");
    }

    @Test
    void testNoPathFound() {
        List<RouteStep> path = navService.getDirections("SCI_101", "FAKE_123");
        assertTrue(path.isEmpty(), "Should return empty list when no valid path exists");
    }
}