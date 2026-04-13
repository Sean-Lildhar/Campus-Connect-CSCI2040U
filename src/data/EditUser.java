package data;

import model.Admin;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV format:
 * username,password,role
 * role is either "admin" or "user" (defaults to "user").
 * Passwords are stored as plain text
 */
public class EditUser {

    private final String FILE_PATH;

    public EditUser() {
        this.FILE_PATH = "data/users.csv";
    }

    public EditUser(String filePath) {
        this.FILE_PATH = filePath;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length < 2) continue;
                String role;
                if (parts.length >= 3) {
                    role = parts[2].trim();
                } else {
                    role = "user";
                }
                if ("admin".equalsIgnoreCase(role)) {
                    users.add(new Admin(parts[0].trim(), parts[1].trim()));
                } else {
                    users.add(new User(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("EditUser.getAllUsers error: " + e.getMessage());
        }
        return users;
    }

    public User authenticate(String username, String password) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public boolean usernameExists(String username) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equals(username)) return true;
        }
        return false;
    }

    public boolean createUser(String username, String password) {
        if (usernameExists(username)) return false;
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(username + "," + password + ",user");
            return true;
        } catch (IOException e) {
            System.err.println("EditUser.createUser error: " + e.getMessage());
            return false;
        }
    }

    public void updateUserRole(String targetUsername, String role) {
        List<User> users = getAllUsers();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (User u : users) {
                String r;
                if (u.getUsername().equals(targetUsername)) {
                    r = role;
                } else {
                    if (u.isAdmin()) {
                        r = "admin";
                    } else {
                        r = "user";
                    }
                }
                pw.println(u.getUsername() + "," + u.getPassword() + "," + r);
            }
        } catch (IOException e) {
            System.err.println("EditUser.updateUserRole error: " + e.getMessage());
        }
    }

}
