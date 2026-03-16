package data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditFavouritesTest {

    private static final String TEST_DIR = "test-data";
    private static final String TEST_FILE = "test-data/favourites.csv";

    private EditFavourites editFavourites;

    @BeforeEach
    void setUp() throws Exception {
        new File(TEST_DIR).mkdirs();
        try (PrintWriter pw = new PrintWriter(TEST_FILE)) {
            pw.println("user,101,202,303");
            pw.println("admin,404");
        }
        editFavourites = new EditFavourites(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    //getFavourites

    @Test
    void testGetFavourites_existingUser_returnsCorrectList() {
        List<String> favs = editFavourites.getFavourites("user");
        assertEquals(3, favs.size());
        assertTrue(favs.contains("101"));
        assertTrue(favs.contains("202"));
        assertTrue(favs.contains("303"));
    }

    @Test
    void testGetFavourites_singleFavourite() {
        List<String> favs = editFavourites.getFavourites("admin");
        assertEquals(1, favs.size());
        assertEquals("404", favs.get(0));
    }

    @Test
    void testGetFavourites_unknownUser_returnsEmptyList() {
        List<String> favs = editFavourites.getFavourites("fakeAccount");
        assertTrue(favs.isEmpty());
    }

    @Test
    void testGetFavourites_missingFile_returnsEmptyList() {
        new File(TEST_FILE).delete();
        assertTrue(editFavourites.getFavourites("user").isEmpty());
    }

    //addFavourite

    @Test
    void testAddFavourite_newRoom_returnsTrue() {
        assertTrue(editFavourites.addFavourite("user", "505"));
    }

    @Test
    void testAddFavourite_newRoom_isPersisted() {
        editFavourites.addFavourite("user", "505");
        assertTrue(editFavourites.getFavourites("user").contains("505"));
    }

    @Test
    void testAddFavourite_duplicate_returnsFalse() {
        assertFalse(editFavourites.addFavourite("user", "101"));
    }

    @Test
    void testAddFavourite_duplicate_doesNotAddAgain() {
        editFavourites.addFavourite("user", "101");
        long count = editFavourites.getFavourites("user")
                .stream().filter(r -> r.equals("101")).count();
        assertEquals(1, count);
    }

    @Test
    void testAddFavourite_newUser_createsEntry() {
        editFavourites.addFavourite("fakeAccount", "606");
        List<String> favs = editFavourites.getFavourites("fakeAccount");
        assertEquals(1, favs.size());
        assertEquals("606", favs.get(0));
    }

    @Test
    void testAddFavourite_doesNotAffectOtherUsers() {
        editFavourites.addFavourite("user", "505");
        List<String> adminFavs = editFavourites.getFavourites("admin");
        assertEquals(1, adminFavs.size());
    }

    //writeFavourites

    @Test
    void testWriteFavourites_overwritesExistingList() {
        List<String> newFavs = Arrays.asList("999", "888");
        editFavourites.writeFavourites("user", newFavs);
        List<String> result = editFavourites.getFavourites("user");
        assertEquals(2, result.size());
        assertTrue(result.contains("999"));
        assertTrue(result.contains("888"));
    }

    @Test
    void testWriteFavourites_newUser_createsEntry() {
        List<String> newFavs = Arrays.asList("111", "222");
        editFavourites.writeFavourites("fakeAccount", newFavs);
        List<String> result = editFavourites.getFavourites("fakeAccount");
        assertEquals(2, result.size());
    }

    @Test
    void testWriteFavourites_doesNotAffectOtherUsers() {
        List<String> newFavs = List.of("999");
        editFavourites.writeFavourites("user", newFavs);
        List<String> adminFavs = editFavourites.getFavourites("admin");
        assertEquals(1, adminFavs.size());
        assertEquals("404", adminFavs.get(0));
    }

    @Test
    void testWriteFavourites_emptyList_clearsEntries() {
        editFavourites.writeFavourites("user", List.of());
        assertTrue(editFavourites.getFavourites("user").isEmpty());
    }
}
