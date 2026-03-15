package gui;

import data.EditLocation;
import model.Location;

import javax.swing.*;
import java.awt.*;


public class AdminScreen extends JDialog {

    private final Location location;
    private final EditLocation editLocation;
    private JComboBox<String> statusDropdown;

    public AdminScreen(JFrame parent,
                       Location location,
                       EditLocation editLocation) {
        super(parent, "Edit Room  -  " + location.getRoomNumber(), true);
        this.location = location;
        this.editLocation = editLocation;

        setSize(310, 230);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(7, 5, 7, 5);

        //Room Type
        g.gridx = 0;
        g.gridy = 0;
        panel.add(new JLabel("Room Type:"), g);
        g.gridx = 1;
        panel.add(new JLabel(capitalize(location.getLocationType())), g);

        //Room Number
        g.gridx = 0;
        g.gridy = 1;
        panel.add(new JLabel("Room Number:"), g);
        g.gridx = 1;
        panel.add(new JLabel(location.getRoomNumber()), g);

        //Status dropdown
        g.gridx = 0;
        g.gridy = 2;
        panel.add(new JLabel("Status:"), g);
        statusDropdown = new JComboBox<>(new String[]{"open", "closed"});
        statusDropdown.setSelectedItem(location.getStatus());
        g.gridx = 1;
        panel.add(statusDropdown, g);

        //Save button
        JButton saveBtn = new JButton("Save");
        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 2;
        g.insets = new Insets(18, 5, 5, 5);
        panel.add(saveBtn, g);

        saveBtn.addActionListener(e -> handleSave());
        add(panel);
    }

    private void handleSave() {
        String newStatus = (String) statusDropdown.getSelectedItem();
        location.setStatus(newStatus);
        editLocation.updateLocation(location);
        JOptionPane.showMessageDialog(this,
                "Room " + location.getRoomNumber()
                        + " status updated to \"" + newStatus + "\".",
                "Saved", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
