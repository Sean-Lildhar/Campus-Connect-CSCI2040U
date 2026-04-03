package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Hashtable;

public class ReviewFrame extends JFrame{
    private JTextArea textAreaReview;
    private JSlider starSlider;

    public void ReviewFrame(String roomNumber, String user) {
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
        //submitButton.addActionListener(e -> storeReview(this.getRoomNumber()));
        submitPanel.add(submitButton);

        this.add(starSlider, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(submitPanel, BorderLayout.SOUTH);

        System.out.println("leaving Review");
    }

    private void storeReview(String roomNumber, String name, int rating, String reviewText) {

    }
}
