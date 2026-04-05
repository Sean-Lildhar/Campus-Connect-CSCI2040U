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

        // Create slider (min=0, max=10, initial=7 to represent "Normal" area)
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
        submitButton.addActionListener(
                e -> {
                    userReview.addReview(roomNumber,user,starSlider.getValue(),textAreaReview.getText());
                    ReviewFrame.this.dispose();
                });
        submitPanel.add(submitButton);
        this.add(starSlider, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(submitPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void viewLocationReviews(String roomNumber){
        if(new EditReviews().getReviewsByRoom(roomNumber).isEmpty()){
            JOptionPane.showMessageDialog(this, "No Reviews", "Reviews", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            this.setTitle("Reviews");
            this.setSize(400, 400);
            this.setLocationRelativeTo(null);
            this.setLayout(new BorderLayout());

            JPanel ratingPanel = new JPanel();
            JLabel reatingsLabel = new JLabel("Averager Rating: " + String.valueOf(new EditReviews().getAverageRating(roomNumber)) + " out of 5 Stars");
            ratingPanel.add(reatingsLabel);

            textAreaLocationReview = new JTextArea();
            textAreaLocationReview.setText(new EditReviews().getReviewsByRoom(roomNumber));
            textAreaLocationReview.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textAreaLocationReview);

            this.add(ratingPanel, BorderLayout.NORTH);
            this.add(scrollPane, BorderLayout.CENTER);
            this.setVisible(true);
        }
    }
}
