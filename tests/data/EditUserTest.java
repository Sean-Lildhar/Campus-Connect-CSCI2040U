package data;

import model.Admin;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditUserTest {

    private static final String TEST_DIR = "test-data";
    private static final String TEST_FILE = "test-data/users.csv";

    private EditUser editUser;

    @BeforeEach
    void setUp() throws Exception {
        new File(TEST_DIR).mkdirs();
        try (PrintWriter pw = new PrintWriter(TEST_FILE)) {
            pw.println("user,user,user");
            pw.println("admin,user,admin");
        }
        editUser = new EditUser(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    //getAllUsers

    @Test
    void testGetAllUsers_returnsCorrectCount() {
        assertEquals(2, editUser.getAllUsers().size());
    }

    @Test
    void testGetAllUsers_userRoleCreatesUser() {
        List<User> users = editUser.getAllUsers();
        User user = users.stream()
                .filter(u -> u.getUsername().equals("user"))
                .findFirst().orElse(null);
        assertNotNull(user);
        assertFalse(user.isAdmin());
    }

    @Test
    void testGetAllUsers_adminRoleCreatesAdmin() {
        List<User> users = editUser.getAllUsers();
        User admin = users.stream()
                .filter(u -> u.getUsername().equals("admin"))
                .findFirst().orElse(null);
        assertNotNull(admin);
        assertTrue(admin.isAdmin());
        assertInstanceOf(Admin.class, admin);
    }

    @Test
    void testGetAllUsers_emptyFile_returnsEmptyList() throws Exception {
        new File(TEST_FILE).delete();
        new File(TEST_FILE).createNewFile();
        assertTrue(editUser.getAllUsers().isEmpty());
    }

    @Test
    void testGetAllUsers_missingFile_returnsEmptyList() {
        new File(TEST_FILE).delete();
        assertTrue(editUser.getAllUsers().isEmpty());
    }

    //usernameExists

    @Test
    void testUsernameExists_existingUser_returnsTrue() {
        assertTrue(editUser.usernameExists("user"));
    }

    @Test
    void testUsernameExists_nonExistentUser_returnsFalse() {
        assertFalse(editUser.usernameExists("fakeAccount"));
    }

    @Test
    void testUsernameExists_caseSensitive() {
        assertFalse(editUser.usernameExists("User")); //"user" exists, "User" does not
    }

    //authenticate

    @Test
    void testAuthenticate_validCredentials_returnsUser() {
        User result = editUser.authenticate("user", "user");
        assertNotNull(result);
        assertEquals("user", result.getUsername());
    }

    @Test
    void testAuthenticate_wrongPassword_returnsNull() {
        assertNull(editUser.authenticate("user", "wrongPassword"));
    }

    @Test
    void testAuthenticate_wrongUsername_returnsNull() {
        assertNull(editUser.authenticate("fakeAccount", "wrongPassword"));
    }

    @Test
    void testAuthenticate_adminUser_returnsAdminInstance() {
        User result = editUser.authenticate("admin", "user");
        assertNotNull(result);
        assertTrue(result.isAdmin());
    }

    @Test
    void testAuthenticate_emptyCredentials_returnsNull() {
        assertNull(editUser.authenticate("", ""));
    }

    //createUser

    @Test
    void testCreateUser_newUser_returnsTrue() {
        assertTrue(editUser.createUser("temp", "temp"));
    }

    @Test
    void testCreateUser_newUser_isPersisted() {
        editUser.createUser("temp", "temp");
        assertTrue(editUser.usernameExists("temp"));
    }

    @Test
    void testCreateUser_newUser_defaultsToUserRole() {
        editUser.createUser("temp", "temp");
        User temp = editUser.authenticate("temp", "temp");
        assertNotNull(temp);
        assertFalse(temp.isAdmin());
    }

    @Test
    void testCreateUser_duplicateUsername_returnsFalse() {
        assertFalse(editUser.createUser("user", "newPassword"));
    }

    @Test
    void testCreateUser_duplicate_doesNotOverwrite() {
        editUser.createUser("user", "newPassword");
        User user = editUser.authenticate("user", "user");
        assertNotNull(user); //original password still works
    }

    //updateUserRole

    @Test
    void testUpdateUserRole_userToAdmin() {
        editUser.updateUserRole("user", "admin");
        User user = editUser.authenticate("user", "user");
        assertNotNull(user);
        assertTrue(user.isAdmin());
    }

    @Test
    void testUpdateUserRole_adminToUser() {
        editUser.updateUserRole("admin", "user");
        User admin = editUser.authenticate("admin", "user");
        assertNotNull(admin);
        assertFalse(admin.isAdmin());
    }

    @Test
    void testUpdateUserRole_doesNotAffectOtherUsers() {
        editUser.updateUserRole("user", "admin");
        User other = editUser.authenticate("admin", "user");
        assertNotNull(other);
        assertTrue(other.isAdmin()); //other should still be admin
    }

    @Test
    void testUpdateUserRole_passwordPreserved() {
        editUser.updateUserRole("admin", "admin");
        User user = editUser.authenticate("admin", "user");
        assertNotNull(user); //original password still valid
    }
}
