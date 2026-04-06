package gui;

import data.EditReviews;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Hashtable;
import java.util.List;

public class ReviewFrame extends JFrame{
    private JTextArea textAreaReview;
    private JTextArea textAreaLocationReview;
    private JSlider starSlider;

    public void leaveReviewFrame(String roomNumber, String user) {
        this.setTitle("Review System");
        this.setLayout(new BorderLayout());
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);

        starSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, 0);
        starSlider.setMajorTickSpacing(1);
        starSlider.setPaintTicks(true);
        starSlider.setSnapToTicks(true);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0,  new JLabel("0 Stars"));
        labelTable.put(5,  new JLabel("5 Stars"));
        starSlider.setLabelTable(labelTable);
        starSlider.setPaintLabels(true);
        starSlider.setBorder(BorderFactory.createTitledBorder("Rate Your experience out of 5 stars"));

        textAreaReview = new JTextArea();
        textAreaReview.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textAreaReview);

        JPanel submitPanel = new JPanel();
        JButton submitButton = new JButton("Submit Review");
        EditReviews userReview = new EditReviews();

        submitButton.addActionListener(e -> {
            String reviewText = textAreaReview.getText().trim();

            if (reviewText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter some text for your review before submitting.",
                        "Review Text Required",
                        JOptionPane.WARNING_MESSAGE);
                return; // Exit the listener so the review is NOT saved
            }

            userReview.addReview(roomNumber, user, starSlider.getValue(), reviewText);
            ReviewFrame.this.dispose();
        });

        submitPanel.add(submitButton);
        this.add(starSlider, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(submitPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void viewLocationReviews(String roomNumber) {
        EditReviews reviewData = new EditReviews();
        String reviewsText = reviewData.getReviewsByRoom(roomNumber);

        if (reviewsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Reviews", "Reviews", JOptionPane.INFORMATION_MESSAGE);
        } else {
            this.setTitle("Reviews");
            this.setSize(400, 400);
            this.setLocationRelativeTo(null);
            this.setLayout(new BorderLayout());

            JPanel ratingPanel = new JPanel();

            double avg = reviewData.getAverageRating(roomNumber);
            String ratingDisplay;

            if (avg <= 0.0) {
                ratingDisplay = "No Ratings Yet";
            } else {
                ratingDisplay = "Average Rating: " + avg + " out of 5 Stars";
            }

            JLabel ratingsLabel = new JLabel(ratingDisplay);
            ratingPanel.add(ratingsLabel);

            JTextArea textAreaReviews = new JTextArea();
            textAreaReviews.setText(reviewsText);
            textAreaReviews.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textAreaReviews);

            this.add(ratingPanel, BorderLayout.NORTH);
            this.add(scrollPane, BorderLayout.CENTER);
            this.setVisible(true);
        }
    }
}
