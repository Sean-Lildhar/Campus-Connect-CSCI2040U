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
            pw.println("# Test graph (new format)");

            // Floor 1 classrooms
            pw.println("SCI1620,classroom,open,1,Lab Hallway F1,2");
            pw.println("SCI1640,classroom,open,1,Lab Hallway F1,2");

            // Floor 2 classroom
            pw.println("SCI2620,classroom,open,2,Lab Hallway F2,2");

            // Hallways
            pw.println("Lab Hallway F1,hallway,open,1,SCI1620,2,SCI1640,2,SCI_STAIRS_F1,1");
            pw.println("Lab Hallway F2,hallway,open,2,SCI2620,2,SCI_STAIRS_F2,1");

            // Stairs
            pw.println("SCI_STAIRS_F1,stairs,open,1,Lab Hallway F1,1,SCI_STAIRS_F2,1");
            pw.println("SCI_STAIRS_F2,stairs,open,2,Lab Hallway F2,1,SCI_STAIRS_F1,1");
        }

        navService = new DijkstraNavigationService(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testSameRoom_returnsEmptyList() {
        List<RouteStep> steps = navService.getDirections("SCI1620", "SCI1620");
        assertNotNull(steps);
        assertTrue(steps.isEmpty());
    }

    @Test
    void testUnknownStart_returnsEmptyList() {
        assertTrue(navService.getDirections("FAKE_START", "SCI1640").isEmpty());
    }

    @Test
    void testUnknownEnd_returnsEmptyList() {
        assertTrue(navService.getDirections("SCI1620", "FAKE_END").isEmpty());
    }

    @Test
    void testBothUnknown_returnsEmptyList() {
        assertTrue(navService.getDirections("FAKE_START", "FAKE_END").isEmpty());
    }

    @Test
    void testSameFloor_returnsNonEmptyPath() {
        List<RouteStep> steps = navService.getDirections("SCI1620", "SCI1640");
        assertFalse(steps.isEmpty());
    }

    @Test
    void testSameFloor_noStairsMentioned() {
        List<RouteStep> steps = navService.getDirections("SCI1620", "SCI1640");

        boolean mentionsStairs = steps.stream()
                .anyMatch(s -> s.getInstruction().toLowerCase().contains("stair"));

        assertFalse(mentionsStairs);
    }

    @Test
    void testShortestPath_onlyTwoSteps() {
        List<RouteStep> steps = navService.getDirections("SCI1620", "SCI1640");
        assertEquals(2, steps.size()); // room → hallway → room
    }

    @Test
    void testCrossFloor_pathNotEmpty() {
        List<RouteStep> steps = navService.getDirections("SCI1620", "SCI2620");
        assertFalse(steps.isEmpty());
    }

    @Test
    void testCrossFloor_mentionsStairs() {
        List<RouteStep> steps = navService.getDirections("SCI1620", "SCI2620");

        boolean usesStairs = steps.stream()
                .anyMatch(s -> s.getInstruction().toLowerCase().contains("stair")
                        || s.getInstruction().toLowerCase().contains("elevator"));

        assertTrue(usesStairs, "Cross-floor route must mention stairs or elevator");
    }

    @Test
    void testRouteSteps_haveValidInstructions() {
        List<RouteStep> steps = navService.getDirections("SCI1620", "SCI2620");

        for (RouteStep step : steps) {
            assertNotNull(step.getInstruction());
            assertFalse(step.getInstruction().isBlank());
        }
    }

    @Test
    void testMissingGraphFile_returnsEmptyList() {
        DijkstraNavigationService broken =
                new DijkstraNavigationService("test-data/nonexistent.csv");

        assertTrue(broken.getDirections("SCI1620", "SCI1640").isEmpty());
    }
}