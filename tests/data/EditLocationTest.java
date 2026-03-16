package data;

import model.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditLocationTest {

    private static final String TEST_DIR = "test-data";
    private static final String TEST_FILE = "test-data/locations.csv";

    private EditLocation editLocation;

    @BeforeEach
    void setUp() throws Exception {
        new File(TEST_DIR).mkdirs();
        try (PrintWriter pw = new PrintWriter(TEST_FILE)) {
            pw.println("101,classroom,open");
            pw.println("202,restaurant,closed");
            pw.println("303,water fountain,open");
            pw.println("404,washroom,open");
        }
        editLocation = new EditLocation(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    //getAllLocations

    @Test
    void testGetAllLocations_returnsCorrectCount() {
        assertEquals(4, editLocation.getAllLocations().size());
    }

    @Test
    void testGetAllLocations_parsesRoomNumberCorrectly() {
        List<Location> locs = editLocation.getAllLocations();
        assertTrue(locs.stream().anyMatch(l -> l.getRoomNumber().equals("101")));
    }

    @Test
    void testGetAllLocations_parsesWaterFountainType() {
        //"water fountain" contains a space - must not be split incorrectly
        List<Location> locs = editLocation.getAllLocations();
        assertTrue(locs.stream().anyMatch(l -> l.getLocationType().equals("water fountain")));
    }

    @Test
    void testGetAllLocations_parsesStatusCorrectly() {
        List<Location> locs = editLocation.getAllLocations();
        Location restaurant = locs.stream()
                .filter(l -> l.getRoomNumber().equals("202"))
                .findFirst().orElse(null);
        assertNotNull(restaurant);
        assertEquals("closed", restaurant.getStatus());
    }

    @Test
    void testGetAllLocations_missingFile_returnsEmptyList() {
        new File(TEST_FILE).delete();
        assertTrue(editLocation.getAllLocations().isEmpty());
    }

    @Test
    void testGetAllLocations_emptyFile_returnsEmptyList() throws Exception {
        new File(TEST_FILE).delete();
        new File(TEST_FILE).createNewFile();
        assertTrue(editLocation.getAllLocations().isEmpty());
    }

    //getLocationsByType

    @Test
    void testGetLocationsByType_classroom() {
        List<Location> result = editLocation.getLocationsByType("classroom");
        assertEquals(1, result.size());
        assertEquals("101", result.get(0).getRoomNumber());
    }

    @Test
    void testGetLocationsByType_caseInsensitive() {
        List<Location> result = editLocation.getLocationsByType("CLASSROOM");
        assertEquals(1, result.size());
    }

    @Test
    void testGetLocationsByType_waterFountain() {
        List<Location> result = editLocation.getLocationsByType("water fountain");
        assertEquals(1, result.size());
        assertEquals("303", result.get(0).getRoomNumber());
    }

    @Test
    void testGetLocationsByType_noMatch_returnsEmptyList() {
        List<Location> result = editLocation.getLocationsByType("gym");
        assertTrue(result.isEmpty());
    }

    //getLocationByRoom

    @Test
    void testGetLocationByRoom_found() {
        Location loc = editLocation.getLocationByRoom("101");
        assertNotNull(loc);
        assertEquals("classroom", loc.getLocationType());
    }

    @Test
    void testGetLocationByRoom_notFound_returnsNull() {
        assertNull(editLocation.getLocationByRoom("999"));
    }

    @Test
    void testGetLocationByRoom_correctStatus() {
        Location loc = editLocation.getLocationByRoom("202");
        assertNotNull(loc);
        assertEquals("closed", loc.getStatus());
    }

    //updateLocation

    @Test
    void testUpdateLocation_statusChangePersisted() {
        Location loc = editLocation.getLocationByRoom("101");
        assertNotNull(loc);
        loc.setStatus("closed");
        editLocation.updateLocation(loc);

        Location updated = editLocation.getLocationByRoom("101");
        assertNotNull(updated);
        assertEquals("closed", updated.getStatus());
    }

    @Test
    void testUpdateLocation_doesNotAffectOtherLocations() {
        Location loc = editLocation.getLocationByRoom("101");
        assertNotNull(loc);
        loc.setStatus("closed");
        editLocation.updateLocation(loc);

        Location other = editLocation.getLocationByRoom("202");
        assertNotNull(other);
        assertEquals("closed", other.getStatus()); // 202 was already closed
    }

    @Test
    void testUpdateLocation_totalCountUnchanged() {
        Location loc = editLocation.getLocationByRoom("101");
        assertNotNull(loc);
        loc.setStatus("closed");
        editLocation.updateLocation(loc);

        assertEquals(4, editLocation.getAllLocations().size());
    }

    @Test
    void testUpdateLocation_typePreservedAfterUpdate() {
        Location loc = editLocation.getLocationByRoom("101");
        assertNotNull(loc);
        loc.setStatus("closed");
        editLocation.updateLocation(loc);

        Location updated = editLocation.getLocationByRoom("101");
        assertNotNull(updated);
        assertEquals("classroom", updated.getLocationType());
    }
}
