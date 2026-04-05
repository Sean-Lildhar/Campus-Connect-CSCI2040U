package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class EditReviews {

    private final String FILE_PATH;

    public EditReviews() {
        this.FILE_PATH = "data/reviews.csv";
    }

    public EditReviews(String filePath) {
        this.FILE_PATH = filePath;
    }

    public void addReview(String roomNumber, String user, int rating, String reviewText) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(roomNumber + "," + user + "," + rating + "," + reviewText);
        } catch (IOException e) {
            System.err.println("EditReviews.addReview error: " + e.getMessage());
        }
    }

    public List<String[]> getAllReviews() {
        List<String[]> reviews = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return reviews;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    reviews.add(parts);
                }
            }
        } catch (IOException e) {
            System.err.println("EditReviews.getAllReviews error: " + e.getMessage());
        }
        return reviews;
    }

    public double getAverageRating(String roomNumber) {
        List<Integer> ratings = new ArrayList<>();
        int total = 0;

        for (String[] review : getAllReviews()) {
            if (review[0].trim().equals(roomNumber)) {
                ratings.add(Integer.parseInt(review[2]));
            }
        }

        if (ratings.isEmpty()) {
            return 0.0;
        }

        for (int num : ratings) {
            total += num;
        }

        double avg = (double) total / ratings.size();
        return Double.parseDouble(String.format("%.1f", avg));
    }

    public String getReviewsByRoom(String roomNumber) {
        StringBuilder result = new StringBuilder();
        for (String[] review : getAllReviews()) {
            if (review[0].trim().equals(roomNumber)) {
                result.append("User: ").append(review[1])
                        .append("\nRating: ").append(review[2]).append(" out of 5 stars")
                        .append("\nReview: ").append(review[3])
                        .append("\n\n");
            }
        }
        return result.toString();
    }
}
