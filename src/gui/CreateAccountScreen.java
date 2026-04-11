package gui;

import data.EditUser;

import javax.swing.*;
import java.awt.*;

/**
 * Check that:
 * - the username is not already taken
 * - the two password fields match
 * All new accounts default to the "user" (non-admin) role.
 */
public class CreateAccountScreen extends JDialog {

    private final EditUser editUser;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField rePasswordField;

    public CreateAccountScreen(JFrame parent, EditUser editUser) {
        super(parent, "Create Account", true);
        this.editUser = editUser;
        setSize(340, 230);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 5, 6, 5);

        //Username
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 1;
        panel.add(new JLabel("Username:"), g);
        usernameField = new JTextField(15);
        g.gridx = 1;
        panel.add(usernameField, g);

        //Password
        g.gridx = 0;
        g.gridy = 1;
        panel.add(new JLabel("Password:"), g);
        passwordField = new JPasswordField(15);
        g.gridx = 1;
        panel.add(passwordField, g);

        //Re-enter Password
        g.gridx = 0;
        g.gridy = 2;
        panel.add(new JLabel("Re-enter Password:"), g);
        rePasswordField = new JPasswordField(15);
        g.gridx = 1;
        panel.add(rePasswordField, g);

        //Create button
        JButton createBtn = new JButton("Create Account");
        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 2;
        g.insets = new Insets(15, 5, 5, 5);
        panel.add(createBtn, g);

        createBtn.addActionListener(e -> handleCreate());
        rePasswordField.addActionListener(e -> handleCreate());
        add(panel);
    }

    private void handleCreate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String rePassword = new String(rePasswordField.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (editUser.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already in use",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(rePassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        editUser.createUser(username, password);
        JOptionPane.showMessageDialog(this, "Account created successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
