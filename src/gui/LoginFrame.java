package gui;

import data.EditUser;
import data.EditFavourites;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * The application entry screen.
 * Allows users to log in or open the Create Account
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton toggleVisibilityButton;
    private JButton loginButton;
    private JButton createAccountButton;

    private final EditUser editUser;

    public LoginFrame() {
        this.editUser = new EditUser();
        setTitle("Campus Navigator  —  Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 260);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 5, 5, 5);

        //Title
        JLabel title = new JLabel("Campus Navigator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 3;
        main.add(title, g);

        //Username row
        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = 1;
        main.add(new JLabel("Username:"), g);

        usernameField = new JTextField(15);
        g.gridx = 1;
        g.gridwidth = 2;
        main.add(usernameField, g);

        //Password row
        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = 2;
        main.add(new JLabel("Password:"), g);

        passwordField = new JPasswordField(15);
        g.gridx = 1;
        g.gridwidth = 1;
        main.add(passwordField, g);

        toggleVisibilityButton = new JButton("Show");
        toggleVisibilityButton.setMargin(new Insets(2, 6, 2, 6));
        g.gridx = 2;
        main.add(toggleVisibilityButton, g);

        //Buttons row
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");
        btnPanel.add(loginButton);
        btnPanel.add(createAccountButton);

        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 3;
        g.insets = new Insets(18, 5, 5, 5);
        main.add(btnPanel, g);

        add(main);
        passwordVis();
    }

    private void passwordVis() {
        //Toggle password visibility
        toggleVisibilityButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == '\0') {
                passwordField.setEchoChar('*');
                toggleVisibilityButton.setText("Show");
            } else {
                passwordField.setEchoChar('\0');
                toggleVisibilityButton.setText("Hide");
            }
        });

        loginButton.addActionListener(e -> handleLogin());
        //Allow pressing Enter in the password field
        passwordField.addActionListener(e -> handleLogin());

        createAccountButton.addActionListener(e -> {
            CreateAccountScreen dlg = new CreateAccountScreen(this, editUser);
            dlg.setVisible(true);
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid username and password",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = editUser.authenticate(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            EditFavourites editFav = new EditFavourites();
            user.setFavouriteLocations(editFav.getFavourites(username));

            dispose();
            MainFrame mainFrame = new MainFrame(user, editUser);
            mainFrame.setVisible(true);
        }
    }
}
