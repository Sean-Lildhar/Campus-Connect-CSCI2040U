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
        navService = new DijkstraNavigationService();
    }

    @Test
    void testSameRoomNavigation() {
        List<RouteStep> path = navService.getDirections("SCI1620", "SCI1620");

        assertNotNull(path);
        assertTrue(path.isEmpty(), "Path to same room should be empty");
    }

    @Test
    void testSameFloorNavigation() {
        List<RouteStep> path = navService.getDirections("SCI1620", "SCI1140");

        assertFalse(path.isEmpty(), "Path should not be empty");

        boolean hasHallway = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("hallway"));

        assertTrue(hasHallway, "Same floor navigation should include hallway movement");
    }

    @Test
    void testCrossFloorNavigation() {
        List<RouteStep> path = navService.getDirections("SCI1620", "SCI2620");

        assertFalse(path.isEmpty(), "Path should not be empty");

        boolean usesStairs = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("stairs"));

        boolean mentionsFloor = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("floor"));

        assertTrue(usesStairs, "Different floors path must use stairs");
        assertTrue(mentionsFloor, "Instructions should mention floor change");
    }

//    @Test
//    void testCrossBuildingNavigation() {
//        List<RouteStep> path = navService.getDirections("SCI1620", "BIT2080");
//
//        assertFalse(path.isEmpty(), "Path should not be empty");
//
//        boolean exitsBuilding = path.stream()
//                .anyMatch(step -> step.getInstruction().toLowerCase().contains("exit"));
//
//        boolean mentionsDestination = path.stream()
//                .anyMatch(step -> step.getInstruction().toLowerCase().contains("head to"));
//
//        assertTrue(exitsBuilding, "Should mention exiting the building");
//        assertTrue(mentionsDestination, "Should mention heading to another building");
//    }

    @Test
    void testNoPathFound() {
        List<RouteStep> path = navService.getDirections("SCI1620", "FAKE_123");

        assertNotNull(path);
        assertTrue(path.isEmpty(), "Should return empty list when no valid path exists");
    }

    @Test
    void testNoEmptyInstructions() {
        List<RouteStep> path = navService.getDirections("SCI1620", "SCI1140");

        boolean hasEmpty = path.stream()
                .anyMatch(step -> step.getInstruction().trim().isEmpty());

        assertFalse(hasEmpty, "Instructions should not be empty");
    }
}