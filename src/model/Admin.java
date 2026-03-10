package model;

/**
 * Admins extend Users and have additional permissions:
 * - Change location status (open / closed)
 * - Upgrade a User to Admin
 * - Downgrade an Admin to User
 */
public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public String toString() {
        return username + " (Admin)";
    }
}
