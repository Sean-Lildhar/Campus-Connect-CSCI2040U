package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Clicking a favourite:
 * - automatically fills the Destination field in the main window
 * - closes the popup
 */
public class FavouritesScreen extends JDialog {

    private final List<String> favourites;
    private final JTextField destinationField;

    public FavouritesScreen(JFrame parent,
                            List<String> favourites,
                            JTextField destinationField) {
        super(parent, "Favourite Locations", true);
        this.favourites = favourites;
        this.destinationField = destinationField;

        setSize(300, 380);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 5));

        JLabel header;
        if(favourites.isEmpty()) {
            header = new JLabel("You have no saved favourites.", SwingConstants.CENTER);
        } else{
            header = new JLabel("Click a room to set as destination:", SwingConstants.CENTER);
        }
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));
        header.setFont(new Font("Arial", Font.PLAIN, 13));
        add(header, BorderLayout.NORTH);

        //Scrollable list
        DefaultListModel<String> model = new DefaultListModel<>();
        if (favourites.isEmpty()) {
            model.addElement("No favourites saved.");
        } else {
            for (String room : favourites) {
                model.addElement(room);
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
                if (idx >= 0 && idx < favourites.size()) {
                    destinationField.setText(favourites.get(idx));
                    dispose();
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
}
