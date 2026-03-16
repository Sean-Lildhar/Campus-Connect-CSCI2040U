package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user", "user");
    }

    //getUsername

    @Test
    void testGetUsername() {
        assertEquals("user", user.getUsername());
    }

    //getPassword

    @Test
    void testGetPassword() {
        assertEquals("user", user.getPassword());
    }

    //isAdmin

    @Test
    void testIsAdmin_returnsFalseForUser() {
        assertFalse(user.isAdmin());
    }

    //getFavouriteLocations

    @Test
    void testGetFavouriteLocations_emptyByDefault() {
        assertTrue(user.getFavouriteLocations().isEmpty());
    }

    //setFavouriteLocations

    @Test
    void testSetFavouriteLocations() {
        List<String> favs = Arrays.asList("101", "202", "303");
        user.setFavouriteLocations(favs);
        assertEquals(favs, user.getFavouriteLocations());
    }

    @Test
    void testSetFavouriteLocations_storesDefensiveCopy() {
        List<String> favs = new java.util.ArrayList<>(Arrays.asList("101", "202"));
        user.setFavouriteLocations(favs);
        favs.add("999");
        assertFalse(user.getFavouriteLocations().contains("999"));
    }

    //addFavourite

    @Test
    void testAddFavourite_newRoom_returnsTrue() {
        assertTrue(user.addFavourite("101"));
    }

    @Test
    void testAddFavourite_newRoom_isStored() {
        user.addFavourite("101");
        assertTrue(user.getFavouriteLocations().contains("101"));
    }

    @Test
    void testAddFavourite_duplicate_returnsFalse() {
        user.addFavourite("101");
        assertFalse(user.addFavourite("101"));
    }

    @Test
    void testAddFavourite_duplicate_notAddedTwice() {
        user.addFavourite("101");
        user.addFavourite("101");
        assertEquals(1, user.getFavouriteLocations().size());
    }

    @Test
    void testAddFavourite_multipleRooms() {
        user.addFavourite("101");
        user.addFavourite("202");
        user.addFavourite("303");
        assertEquals(3, user.getFavouriteLocations().size());
    }

    //toString

    @Test
    void testToString_containsUsername() {
        assertTrue(user.toString().contains("user"));
    }
}
