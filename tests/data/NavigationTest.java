package data;

import model.RouteStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testCrossBuildingNavigation() {
        List<RouteStep> path = navService.getDirections("SCI1620", "BIT2080");

        assertFalse(path.isEmpty(), "Path should not be empty");

        boolean exitsBuilding = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("exit"));

        boolean mentionsDestination = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("head to"));

        assertTrue(exitsBuilding, "Should mention exiting the building");
        assertTrue(mentionsDestination, "Should mention heading to another building");
    }

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

    @Test
    void testSIRCtoMainCampus_CrossConlinRoad() {
        List<RouteStep> path = navService.getDirections("SIR4110", "SCI1120");

        assertFalse(path.isEmpty(), "Path from SIRC to SCI should exist");

        boolean mentionsConlin = path.stream()
                .anyMatch(step -> step.getInstruction().contains("Conlin Road"));

        assertTrue(mentionsConlin, "Route from SIRC must mention crossing Conlin Road");
    }

    @Test
    void testMainCampustoSIRC_CrossConlinRoad() {
        List<RouteStep> path = navService.getDirections("BIT1043(men)", "SIR2010");

        boolean mentionsConlin = path.stream()
                .anyMatch(step -> step.getInstruction().contains("Conlin Road"));

        assertTrue(mentionsConlin, "Route to SIRC must mention crossing Conlin Road");
    }

    @Test
    void testInterBuilding_viaFirstFloorOnly() {
        List<RouteStep> path = navService.getDirections("BIT4043", "SHA344");

        assertFalse(path.isEmpty(), "Path should not be empty");

        boolean exitedToQuad = path.stream()
                .anyMatch(step -> step.getInstruction().contains("Exit the building into Polonsky Commons"));

        boolean enteredFromQuad = path.stream()
                .anyMatch(step -> step.getInstruction().contains("Enter Sha Entrance from the Polonsky Commons"));

        boolean wentDown = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("down to floor"));

        boolean wentUp = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("up to floor"));

        assertTrue(exitedToQuad, "Path must involve exiting to Polonsky Commons");
        assertTrue(enteredFromQuad, "Path must involve entering SHA from Polonsky Commons");
        assertTrue(wentDown, "Path must go down to reach the exit");
        assertTrue(wentUp, "Path must go up to reach the destination floor");
    }

    @Test
    void testRestaurantNavigation() {
        List<RouteStep> path = navService.getDirections("SCI4120", "Tim Hortons");

        assertFalse(path.isEmpty());

        boolean arrivesAtTims = path.get(path.size() - 1).getInstruction()
                .contains("Tim Hortons");

        assertTrue(arrivesAtTims, "Final destination should be Tim Hortons");
    }

    @Test
    void testWashroomNavigation_SameFloor() {
        List<RouteStep> path = navService.getDirections("BIT2080", "BIT2067");

        assertFalse(path.isEmpty());

        boolean usesStairs = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("stair"));

        assertFalse(usesStairs, "Same floor navigation should not use stairs");
    }

    @Test
    void testBasementNavigation_SHA() {
        List<RouteStep> path = navService.getDirections("SHA131", "SHA018");

        boolean goesDown = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("down to floor 0"));

        assertTrue(goesDown, "Should be able to navigate to the SHA basement");
    }

    @Test
    void testPolonskyCommons() {
        List<RouteStep> path = navService.getDirections("BIT_ENTRANCE", "SHA_ENTRANCE");

        boolean usesPolosky = path.stream()
                .anyMatch(step -> step.getInstruction().toLowerCase().contains("polonsky commons"));

        assertTrue(usesPolosky, "Building entrances should be connected via the Campus Quad");
    }
}