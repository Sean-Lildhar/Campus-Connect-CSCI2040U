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

    public void addReview(String roomNumber, String username, int rating, String reviewText) {

        String cleanText = reviewText.replace("\n", " ")
                .replace("\r", " ")
                .replace(",", ";");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(roomNumber + "," + username + "," + rating + "," + cleanText);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error saving review: " + e.getMessage());
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
        int total = 0;
        int count = 0;

        for (String[] review : getAllReviews()) {
            if (review[0].trim().equals(roomNumber)) {
                try {
                    int rating = Integer.parseInt(review[2].trim());
                    total += rating;
                    count++;
                } catch (NumberFormatException e) {
                    System.err.println("EditReviews.getAverageRating: Skipping malformed rating for room "
                            + roomNumber + ": " + review[2]);
                }
            }
        }

        if (count == 0) {
            return 0.0;
        }

        double avg = (double) total / count;
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
