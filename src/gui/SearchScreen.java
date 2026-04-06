package gui;

import data.EditFavourites;
import model.Location;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Shows a scrollable list of Location objects matching the chosen type.
 * Clicking a location opens a small option popup with:
 * - "Take me here" -> fills the Destination field and closes the popup
 * - "Add to favourites"  -> adds the room to favourites.csv
 */
public class SearchScreen extends JDialog {

    private final List<Location> locations;
    private final JTextField destinationField;
    private final User currentUser;
    private final EditFavourites favourites;

    public SearchScreen(JFrame parent,
                        List<Location> locations,
                        JTextField destinationField,
                        User currentUser,
                        EditFavourites favourites) {
        super(parent, "Search Results", true);
        this.locations = locations;
        this.destinationField = destinationField;
        this.currentUser = currentUser;
        this.favourites = favourites;

        setSize(380, 420);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 5));

        JLabel header = new JLabel("Click a location to select it:", SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));
        header.setFont(new Font("Arial", Font.PLAIN, 13));
        add(header, BorderLayout.NORTH);

        //Scrollable list
        DefaultListModel<String> model = new DefaultListModel<>();
        if (locations.isEmpty()) {
            model.addElement("No locations found for this type.");
        } else {
            for (Location loc : locations) {
                model.addElement(loc.toString());
            }
        }

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Monospaced", Font.PLAIN, 13));
        list.setFixedCellHeight(26);
        add(new JScrollPane(list), BorderLayout.CENTER);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = list.locationToIndex(e.getPoint());
                if (idx >= 0 && idx < locations.size()) {
                    showLocationOptions(locations.get(idx));
                }
            }
        });

        //Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel south = new JPanel();
        south.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
        south.add(closeBtn);
        add(south, BorderLayout.SOUTH);
    }

    private void showLocationOptions(Location loc) {
        String[] options = {"Take me here", "Add to favourites", "Cancel", "Leave Review", "Check Reviews"};
        int choice = JOptionPane.showOptionDialog(
                this,
                loc.toString(),
                "Location Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            destinationField.setText(loc.getRoomNumber());
            dispose();
        } else if (choice == 1) {
            boolean added = favourites.addFavourite(
                    currentUser.getUsername(), loc.getRoomNumber());
            if (added) {
                currentUser.addFavourite(loc.getRoomNumber());
                JOptionPane.showMessageDialog(this,
                        "Successfully added '" + loc.getRoomNumber() + "' to favourites",
                        "Favourites", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "'" + loc.getRoomNumber() + "' is already in your favourites.",
                        "Favourites", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (choice == 3) {
            this.dispose();
            new ReviewFrame().leaveReviewFrame(loc.getRoomNumber(), currentUser.getUsername());
        } else if (choice == 4) {
            this.dispose();
            new ReviewFrame().viewLocationReviews(loc.getRoomNumber());
        }
    }
}
