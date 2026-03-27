package data;

import model.RouteStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraNavigationTest {

    private static final String TEST_DIR  = "test-data";
    private static final String TEST_FILE = "test-data/graph.csv";

    private DijkstraNavigationService navService;

    @BeforeEach
    void setUp() throws Exception {
        new File(TEST_DIR).mkdirs();
        try (PrintWriter pw = new PrintWriter(TEST_FILE)) {
            pw.println("# Test graph");
            pw.println("A,Room A,BLDX,1,HALL,5");
            pw.println("B,Room B,BLDX,1,HALL,5");
            pw.println("HALL,Floor 1 Hallway,BLDX,1,A,5,B,5,STAIR1,15,EXIT,20");
            pw.println("STAIR1,Stairwell Floor 1,BLDX,1,HALL,15,STAIR2,20");
            pw.println("STAIR2,Stairwell Floor 2,BLDX,2,STAIR1,20,C,5");
            pw.println("C,Room C,BLDX,2,STAIR2,5");
            pw.println("EXIT,Building X Exit,BLDX,1,HALL,20,COURT,15");
            pw.println("COURT,Courtyard,OUTDOOR,0,EXIT,15,D_ENT,20");
            pw.println("D_ENT,Building Y Entrance,BLDY,1,COURT,20,D,5");
            pw.println("D,Room D,BLDY,1,D_ENT,5");
        }
        navService = new DijkstraNavigationService(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testSameRoom_returnsEmptyList() {
        List<RouteStep> steps = navService.getDirections("A", "A");
        assertNotNull(steps);
        assertTrue(steps.isEmpty());
    }

    @Test
    void testUnknownStart_returnsEmptyList() {
        assertTrue(navService.getDirections("FAKE_START", "B").isEmpty());
    }

    @Test
    void testUnknownEnd_returnsEmptyList() {
        assertTrue(navService.getDirections("A", "FAKE_END").isEmpty());
    }

    @Test
    void testBothUnknown_returnsEmptyList() {
        assertTrue(navService.getDirections("FAKE_START", "FAKE_END").isEmpty());
    }

    @Test
    void testSameFloor_returnsNonEmptyPath() {
        List<RouteStep> steps = navService.getDirections("A", "B");
        assertFalse(steps.isEmpty());
    }

    @Test
    void testSameFloor_finalStepArrivesAtDestination() {
        List<RouteStep> steps = navService.getDirections("A", "B");
        assertTrue(steps.get(steps.size() - 1).getInstruction().toLowerCase().contains("arrive"));
    }

    @Test
    void testSameFloor_noStairsOrExitMentioned() {
        List<RouteStep> steps = navService.getDirections("A", "B");
        boolean mentionsStairsOrExit = steps.stream()
                .anyMatch(s -> s.getInstruction().toLowerCase().contains("stair")
                        || s.getInstruction().toLowerCase().contains("exit"));
        assertFalse(mentionsStairsOrExit);
    }
    @Test
    void testCrossFloor_pathNotEmpty() {
        List<RouteStep> steps = navService.getDirections("A", "C");
        assertFalse(steps.isEmpty());
    }

    @Test
    void testCrossFloor_mentionsStairs() {
        List<RouteStep> steps = navService.getDirections("A", "C");
        boolean usesStairs = steps.stream()
                .anyMatch(s -> s.getInstruction().toLowerCase().contains("stair")
                        || s.getInstruction().toLowerCase().contains("elevator"));
        assertTrue(usesStairs, "Cross-floor route must mention stairs or elevator");
    }

    @Test
    void testCrossFloor_noExitMentioned() {
        List<RouteStep> steps = navService.getDirections("A", "C");
        boolean mentionsExit = steps.stream()
                .anyMatch(s -> s.getInstruction().toLowerCase().contains("exit"));
        assertFalse(mentionsExit, "Same-building route must not mention exiting");
    }

    @Test
    void testCrossBuilding_pathNotEmpty() {
        List<RouteStep> steps = navService.getDirections("A", "D");
        assertFalse(steps.isEmpty());
    }

    @Test
    void testCrossBuilding_mentionsExit() {
        List<RouteStep> steps = navService.getDirections("A", "D");
        boolean exitsBuilding = steps.stream()
                .anyMatch(s -> s.getInstruction().toLowerCase().contains("exit"));
        assertTrue(exitsBuilding, "Cross-building route must mention exiting a building");
    }

    @Test
    void testCrossBuilding_finalStepArrivesAtDestination() {
        List<RouteStep> steps = navService.getDirections("A", "D");
        assertTrue(steps.get(steps.size() - 1).getInstruction().toLowerCase().contains("arrive"));
    }

    @Test
    void testShortestPath_AtoB_onlyTwoSteps() {
        // Optimal: A -> HALL -> B (2 hops = 2 steps)
        List<RouteStep> steps = navService.getDirections("A", "B");
        assertEquals(2, steps.size(), "A->B should route A->HALL->B (2 steps)");
    }

    @Test
    void testRouteSteps_waypointIdsAreNonNull() {
        List<RouteStep> steps = navService.getDirections("A", "D");
        for (RouteStep step : steps) {
            assertNotNull(step.getInstruction());
            assertFalse(step.getInstruction().isBlank());
        }
    }

    @Test
    void testMissingGraphFile_returnsEmptyList() {
        DijkstraNavigationService broken = new DijkstraNavigationService("test-data/nonexistent.csv");
        assertTrue(broken.getDirections("A", "B").isEmpty());
    }
}
