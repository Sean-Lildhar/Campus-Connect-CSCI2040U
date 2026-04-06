package data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditReviewsTest {

    private static final String TEST_FILE = "data/test_reviews.csv";
    private EditReviews editReviews;

    @BeforeEach
    void setUp() {
        editReviews = new EditReviews(TEST_FILE);

        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddAndGetAllReviews() {
        editReviews.addReview("SCI1620", "StudentA", 5, "Great lab space!");
        editReviews.addReview("BIT2080", "StudentB", 3, "A bit cramped.");

        List<String[]> allReviews = editReviews.getAllReviews();

        assertEquals(2, allReviews.size(), "Should have exactly 2 reviews in the file");
        assertEquals("SCI1620", allReviews.get(0)[0]);
        assertEquals("Great lab space!", allReviews.get(0)[3]);
    }

    @Test
    void testGetAverageRating_MultipleReviews() {
        editReviews.addReview("SCI2120", "User1", 5, "Excellent");
        editReviews.addReview("SCI2120", "User2", 4, "Good");
        editReviews.addReview("SCI2120", "User3", 2, "Poor lighting");

        double average = editReviews.getAverageRating("SCI2120");

        assertEquals(3.7, average, 0.01, "Average should be correctly rounded to 1 decimal place");
    }

    @Test
    void testGetAverageRating_SingleReview() {
        editReviews.addReview("SHA131", "User1", 4, "Nice");

        double average = editReviews.getAverageRating("SHA131");

        assertEquals(4.0, average);
    }

    @Test
    void testGetReviewsByRoom_Filtering() {
        editReviews.addReview("SIR2010", "Alice", 5, "Fast wifi");
        editReviews.addReview("BIT1042", "Bob", 1, "No outlets");
        editReviews.addReview("SIR2010", "Charlie", 4, "Quiet");

        String result = editReviews.getReviewsByRoom("SIR2010");

        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("Charlie"));
        assertTrue(result.contains("Fast wifi"));
        assertFalse(result.contains("Bob"), "Results should not contain reviews from other rooms");
    }

    @Test
    void testGetReviewsByRoom_EmptyRoom() {
        String result = editReviews.getReviewsByRoom("NON_EXISTENT_ROOM");

        assertTrue(result.isEmpty(), "Should return an empty string for rooms with no reviews");
    }

    @Test
    void testGetAverageRating_NoReviews() {
        double average = editReviews.getAverageRating("EMPTY_ROOM");

        assertEquals(0.0, average, "Should return 0.0 if no reviews exist for a room");
    }
}