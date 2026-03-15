package gui;

import data.EditUser;
import model.User;

import javax.swing.*;
import java.awt.*;

public class UserPerms extends JDialog {

    private final User user;
    private final EditUser editUser;
    private final Runnable onSave;
    private JComboBox<String> roleDropdown;

    public UserPerms(JFrame parent, User user, EditUser editUser, Runnable onSave) {
        super(parent, "Edit Permissions  -   " + user.getUsername(), true);
        this.user = user;
        this.editUser = editUser;
        this.onSave = onSave;

        setSize(300, 210);
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

        //Username
        g.gridx = 0; g.gridy = 0;
        panel.add(new JLabel("Username:"), g);
        g.gridx = 1;
        panel.add(new JLabel(user.getUsername()), g);

        //Current role
        g.gridx = 0; g.gridy = 1;
        panel.add(new JLabel("Current Role:"), g);
        g.gridx = 1;
        panel.add(new JLabel(user.isAdmin() ? "Admin" : "User"), g);

        //Role dropdown
        g.gridx = 0; g.gridy = 2;
        panel.add(new JLabel("New Role:"), g);
        roleDropdown = new JComboBox<>(new String[]{"user", "admin"});
        roleDropdown.setSelectedItem(user.isAdmin() ? "admin" : "user");
        g.gridx = 1;
        panel.add(roleDropdown, g);

        //Save button
        JButton saveBtn = new JButton("Save");
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        g.insets = new Insets(18, 5, 5, 5);
        panel.add(saveBtn, g);

        saveBtn.addActionListener(e -> handleSave());
        add(panel);
    }

    private void handleSave() {
        String newRole = (String) roleDropdown.getSelectedItem();
        editUser.updateUserRole(user.getUsername(), newRole);
        JOptionPane.showMessageDialog(this,
                user.getUsername() + " is now a " + newRole + ".",
                "Saved", JOptionPane.INFORMATION_MESSAGE);
        onSave.run();
        dispose();
    }
}