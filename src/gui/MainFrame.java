package gui;

import data.*;
import model.Location;
import model.RouteStep;
import model.User;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Layout: JSplitPane  (left | right)
 * Left panel  — empty map placeholder for regular users;
 * admin tree panel (JTree by location type) for admins.
 * Right panel — navigation controls: Starting Location, Destination,
 * location-type dropdown, Navigate / Search / Favourites buttons.
 * A logout button in the top bar returns the user to LoginFrame.
 */
public class MainFrame extends JFrame {

    private final User currentUser;
    private final EditUser editUser;
    private final EditLocation editLocation;
    private final EditFavourites EditFavourites;

    private JTextField startingLocationField;
    private JTextField destinationField;
    private JComboBox<String> locationTypeDropdown;

    private JTree adminTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode treeRoot;

    public MainFrame(User user, EditUser editUser) {
        this.currentUser = user;
        this.editUser = editUser;
        this.editLocation = new EditLocation();
        this.EditFavourites = new EditFavourites();

        setTitle("Campus Connect  - "
                + user.getUsername()
                + (user.isAdmin() ? "  (Admin)" : ""));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 620);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(null);

        initComponents();
    }


    private void initComponents() {
        //Top bar
        add(buildTopBar(), BorderLayout.NORTH);

        //Split pane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPanel(), buildRightPanel());
        split.setDividerLocation(320);
        split.setResizeWeight(0.35);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        String login = "Logged in as:  " + currentUser.getUsername();
        if (currentUser.isAdmin()) {
            login = login + "  [Admin]";
        }
        JLabel info = new JLabel(login);
        info.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        bar.add(info, BorderLayout.WEST);
        bar.add(logout, BorderLayout.EAST);
        return bar;
    }

    //Left Panel
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        if (currentUser.isAdmin()) {
            panel.setBorder(BorderFactory.createTitledBorder("Admin Panel"));
            panel.add(buildAdminTreePanel(), BorderLayout.CENTER);
        } else {
            panel.setBorder(BorderFactory.createTitledBorder("Campus Map"));
            panel.add(new MapPanel(), BorderLayout.CENTER);
        }
        return panel;
    }

    //Tree for Admins
    private JScrollPane buildAdminTreePanel() {
        treeRoot = new DefaultMutableTreeNode("Campus");
        treeModel = new DefaultTreeModel(treeRoot);
        populateTree();

        adminTree = new JTree(treeModel);
        adminTree.setRootVisible(false);
        adminTree.setShowsRootHandles(true);
        expandAllRows();

        adminTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected =
                    (DefaultMutableTreeNode) adminTree.getLastSelectedPathComponent();
            if (selected == null || !selected.isLeaf()) {
                return;
            }
            String roomNumber = selected.getUserObject().toString();
            Location loc = editLocation.getLocationByRoom(roomNumber);
            if (loc != null) {
                AdminScreen dialogue =
                        new AdminScreen(this, loc, editLocation);
                dialogue.setVisible(true);
                rebuildTree();
            }
        });

        return new JScrollPane(adminTree);
    }

    private void populateTree() {
        Map<String, DefaultMutableTreeNode> cats = new LinkedHashMap<>();
        cats.put("restaurant", new DefaultMutableTreeNode("Restaurants"));
        cats.put("washroom", new DefaultMutableTreeNode("Washrooms"));
        cats.put("water fountain", new DefaultMutableTreeNode("Water Fountains"));
        cats.put("classroom", new DefaultMutableTreeNode("Classrooms"));

        for (DefaultMutableTreeNode catNode : cats.values()) {
            treeRoot.add(catNode);
        }

        for (Location loc : editLocation.getAllLocations()) {
            DefaultMutableTreeNode catNode = cats.get(loc.getLocationType().toLowerCase());
            if (catNode != null) {
                catNode.add(new DefaultMutableTreeNode(loc.getRoomNumber()));
            }
        }
    }

    private void rebuildTree() {
        treeRoot.removeAllChildren();
        populateTree();
        treeModel.reload();
        expandAllRows();
    }

    private void expandAllRows() {
        for (int i = 0; i < adminTree.getRowCount(); i++) {
            adminTree.expandRow(i);
        }
    }

    //Right Panel
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("NavigationService"));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(9, 12, 9, 12);

        //Starting Location
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 1;
        g.weightx = 0;
        panel.add(new JLabel("Starting Location:"), g);
        startingLocationField = new JTextField(18);
        g.gridx = 1;
        g.weightx = 1;
        panel.add(startingLocationField, g);

        //Destination
        g.gridx = 0;
        g.gridy = 1;
        g.weightx = 0;
        panel.add(new JLabel("Destination:"), g);
        destinationField = new JTextField(18);
        g.gridx = 1;
        g.weightx = 1;
        panel.add(destinationField, g);

        //Location Type dropdown
        g.gridx = 0;
        g.gridy = 2;
        g.weightx = 0;
        panel.add(new JLabel("Location Type:"), g);
        locationTypeDropdown = new JComboBox<>(
                new String[]{"Select a Category to Search", "restaurant", "washroom", "water fountain", "classroom"});
        g.gridx = 1;
        g.weightx = 1;
        panel.add(locationTypeDropdown, g);

        //Action buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        JButton navButton = new JButton("Navigate");
        JButton searchButton = new JButton("Search");
        JButton favButton = new JButton("Favourites");
        buttons.add(navButton);
        buttons.add(searchButton);
        buttons.add(favButton);

        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 2;
        g.insets = new Insets(20, 12, 9, 12);
        panel.add(buttons, g);

        navButton.addActionListener(e -> handleNavigate());
        searchButton.addActionListener(e -> handleSearch());
        favButton.addActionListener(e -> handleFavourites());

        //Admin User Management
        if (currentUser.isAdmin()) {
            g.gridx = 0;
            g.gridy = 4;
            g.gridwidth = 2;
            g.weightx = 1;
            g.insets = new Insets(16, 12, 2, 12);
            panel.add(new JSeparator(), g);

            g.gridy = 5;
            g.insets = new Insets(2, 12, 4, 12);
            JLabel usersLabel = new JLabel("User Management");
            usersLabel.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(usersLabel, g);

            DefaultListModel<String> userListModel = new DefaultListModel<>();
            JList<String> userList = new JList<>(userListModel);
            userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            userList.setFont(new Font("Monospaced", Font.PLAIN, 12));
            userList.setFixedCellHeight(24);
            refreshUserList(userListModel);

            JScrollPane userScroll = new JScrollPane(userList);
            userScroll.setPreferredSize(new Dimension(0, 160));

            g.gridy = 6;
            g.weighty = 1;
            g.fill = GridBagConstraints.BOTH;
            g.insets = new Insets(0, 12, 12, 12);
            panel.add(userScroll, g);

            userList.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int idx = userList.locationToIndex(e.getPoint());
                    if (idx < 0) return;
                    List<User> allUsers = editUser.getAllUsers();
                    if (idx >= allUsers.size()) return;
                    User selected = allUsers.get(idx);
                    // Prevent admin from editing their own role
                    if (selected.getUsername().equals(currentUser.getUsername())) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "You cannot change your own role.",
                                "Not Allowed", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    UserPerms perm = new UserPerms(
                            MainFrame.this, selected, editUser,
                            () -> refreshUserList(userListModel));
                    perm.setVisible(true);
                }
            });
        } else {
            g.gridx = 0;
            g.gridy = 4;
            g.gridwidth = 2;
            g.weighty = 1;
            panel.add(Box.createVerticalGlue(), g);
        }

        return panel;
    }

    private void refreshUserList(DefaultListModel<String> model) {
        model.clear();
        for (User u : editUser.getAllUsers()) {
            model.addElement((u.isAdmin() ? "[Admin]  " : "[User]   ") + u.getUsername());
        }
    }

    private void handleNavigate() {
        String start = startingLocationField.getText().trim();
        String dest = destinationField.getText().trim();

        //Check for blank or invalid locations
        Location startLoc = editLocation.getLocationByRoom(start);
        Location destLoc = editLocation.getLocationByRoom(dest);

        if (start.isEmpty() || dest.isEmpty() || startLoc == null || destLoc == null) {
            JOptionPane.showMessageDialog(this,
                    "Please Enter a Valid Location",
                    "Invalid Location", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //Check if destination is closed
        if ("closed".equalsIgnoreCase(destLoc.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "This location is currently closed. We apologize for the inconvenience.",
                    "Location Closed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        NavigationService navService = new DijkstraNavigationService();
        List<RouteStep> steps = navService.getDirections(start, dest);

        if (steps.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No path found between '" + start + "' and '" + dest + "'.",
                    "No Route", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.size(); i++) {
            sb.append(i + 1).append(". ").append(steps.get(i).getInstruction()).append("\n");
        }

        JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Directions: " + start + " to " + dest,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleSearch() {
        String type = (String) locationTypeDropdown.getSelectedItem();
        List<Location> results = editLocation.getLocationsByType(type);
        SearchScreen dlg = new SearchScreen(
                this, results, destinationField, currentUser, EditFavourites);
        dlg.setVisible(true);
    }

    private void handleFavourites() {
        List<String> favs = EditFavourites.getFavourites(currentUser.getUsername());
        FavouritesScreen dlg = new FavouritesScreen(this, favs, destinationField);
        dlg.setVisible(true);
    }
}
