package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin("admin", "admin");
    }

    //isAdmin

    @Test
    void testIsAdmin_returnsTrue() {
        assertTrue(admin.isAdmin());
    }

    //Inherited from User

    @Test
    void testGetUsername_inherited() {
        assertEquals("admin", admin.getUsername());
    }

    @Test
    void testGetPassword_inherited() {
        assertEquals("admin", admin.getPassword());
    }

    @Test
    void testAddFavourite_inherited_newRoom() {
        assertTrue(admin.addFavourite("101"));
        assertTrue(admin.getFavouriteLocations().contains("101"));
    }

    @Test
    void testAddFavourite_inherited_duplicate() {
        admin.addFavourite("101");
        assertFalse(admin.addFavourite("101"));
    }

    @Test
    void testAdminIsInstanceOfUser() {
        assertTrue(admin instanceof User);
    }

    @Test
    void testIsAdmin_userReturnsFalse_adminReturnsTrue() {
        User regularUser = new User("user", "pass");
        assertFalse(regularUser.isAdmin());
        assertTrue(admin.isAdmin());
    }

    @Test
    void testToString_containsUsername() {
        assertTrue(admin.toString().contains("admin"));
    }
}
