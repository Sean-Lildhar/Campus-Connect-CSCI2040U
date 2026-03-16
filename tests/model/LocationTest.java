package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    private Location classroom;
    private Location restaurant;
    private Location waterFountain;

    @BeforeEach
    void setUp() {
        classroom = new Location("101", "classroom", "open");
        restaurant = new Location("202", "restaurant", "closed");
        waterFountain = new Location("303", "water fountain", "open");
    }

    //getRoomNumber

    @Test
    void testGetRoomNumber() {
        assertEquals("101", classroom.getRoomNumber());
    }

    @Test
    void testGetRoomNumber_restaurant() {
        assertEquals("202", restaurant.getRoomNumber());
    }

    //getLocationType

    @Test
    void testGetLocationType_classroom() {
        assertEquals("classroom", classroom.getLocationType());
    }

    @Test
    void testGetLocationType_restaurant() {
        assertEquals("restaurant", restaurant.getLocationType());
    }

    @Test
    void testGetLocationType_waterFountain() {
        assertEquals("water fountain", waterFountain.getLocationType());
    }

    //getStatus

    @Test
    void testGetStatus_open() {
        assertEquals("open", classroom.getStatus());
    }

    @Test
    void testGetStatus_closed() {
        assertEquals("closed", restaurant.getStatus());
    }

    //etStatus

    @Test
    void testSetStatus_openToClosed() {
        classroom.setStatus("closed");
        assertEquals("closed", classroom.getStatus());
    }

    @Test
    void testSetStatus_closedToOpen() {
        restaurant.setStatus("open");
        assertEquals("open", restaurant.getStatus());
    }

    @Test
    void testSetStatus_doesNotAffectOtherFields() {
        classroom.setStatus("closed");
        assertEquals("101", classroom.getRoomNumber());
        assertEquals("classroom", classroom.getLocationType());
    }

    //toString

    @Test
    void testToString_containsRoomNumber() {
        assertTrue(classroom.toString().contains("101"));
    }

    @Test
    void testToString_containsLocationType() {
        assertTrue(classroom.toString().contains("classroom"));
    }

    @Test
    void testToString_containsStatus() {
        assertTrue(classroom.toString().contains("open"));
    }

}
