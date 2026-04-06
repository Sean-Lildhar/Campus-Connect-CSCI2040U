package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Displays campus map files from maps/ in a scrollable panel.
 * First select a building, then the floor in two separate dropdowns
 */
public class MapPanel extends JPanel {

    // Building display name
    private static final Map<String, String> BUILDINGS = new LinkedHashMap<>();

    //Floor display names
    private static final Map<String, String[]> FLOORS = new LinkedHashMap<>();

    //Floor filename names
    private static final Map<String, String[]> FLOOR_FILES = new LinkedHashMap<>();
    private static final String MAPS_DIR = "maps";

    static {
        BUILDINGS.put("Science Building", "SCI");
        BUILDINGS.put("Business Building", "BIT");
        BUILDINGS.put("Shawenjigewining Hall", "SHA");
        BUILDINGS.put("Software Informatics and Research Centre", "SIR");

        FLOORS.put("SCI", new String[]{"Floor 1", "Floor 2", "Floor 3", "Floor 4"});
        FLOORS.put("BIT", new String[]{"Floor 1", "Floor 2", "Floor 3", "Floor 4"});
        FLOORS.put("SHA", new String[]{"Basement", "Floor 1", "Floor 2", "Floor 3"});
        FLOORS.put("SIR", new String[]{"Floor 2", "Floor 3", "Floor 4"});

        FLOOR_FILES.put("SCI", new String[]{"Floor1", "Floor2", "Floor3", "Floor4"});
        FLOOR_FILES.put("BIT", new String[]{"Floor1", "Floor2", "Floor3", "Floor4"});
        FLOOR_FILES.put("SHA", new String[]{"Basement", "Floor1", "Floor2", "Floor3"});
        FLOOR_FILES.put("SIR", new String[]{"Floor2", "Floor3", "Floor4"});
    }

    private final JComboBox<String> buildingDropdown;
    private final JComboBox<String> floorDropdown;
    private final JLabel mapLabel;
    private final JScrollPane scrollPane;

    private Point startPoint;
    private Point startScroll;

    public MapPanel() {
        setLayout(new BorderLayout(0, 4));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        //Dropdown panels
        JPanel dropdownPanel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 4, 3, 4);

        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 0;
        dropdownPanel.add(new JLabel("Building:"), g);

        buildingDropdown = new JComboBox<>();
        buildingDropdown.addItem("Select Building");
        for (String name : BUILDINGS.keySet()) {
            buildingDropdown.addItem(name);
        }
        g.gridx = 1;
        g.weightx = 1;
        dropdownPanel.add(buildingDropdown, g);

        g.gridx = 0;
        g.gridy = 1;
        g.weightx = 0;
        dropdownPanel.add(new JLabel("Floor:"), g);

        floorDropdown = new JComboBox<>();
        floorDropdown.addItem("Select Floor");
        floorDropdown.setEnabled(false);
        g.gridx = 1;
        g.weightx = 1;
        dropdownPanel.add(floorDropdown, g);

        add(dropdownPanel, BorderLayout.NORTH);

        //Map image area
        mapLabel = new JLabel();
        mapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mapLabel.setVerticalAlignment(SwingConstants.CENTER);
        mapLabel.setForeground(Color.GRAY);

        scrollPane = new JScrollPane(mapLabel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        add(scrollPane, BorderLayout.CENTER);

        MouseAdapter dragMap = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                JViewport vp = scrollPane.getViewport();
                startScroll = vp.getViewPosition();
                scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (startPoint == null) return;

                int dx = e.getX() - startPoint.x;
                int dy = e.getY() - startPoint.y;

                JViewport vp = scrollPane.getViewport();
                Dimension view = vp.getView().getPreferredSize();
                Dimension extent = vp.getExtentSize();

                int x1 = Math.max(0, view.width - extent.width);
                int y1 = Math.max(0, view.height - extent.height);

                int x2 = Math.max(0, Math.min(x1, startScroll.x - dx));
                int y2 = Math.max(0, Math.min(y1, startScroll.y - dy));

                vp.setViewPosition(new Point(x2, y2));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startPoint = null;
                startScroll = null;
                scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        };

        scrollPane.getViewport().addMouseListener(dragMap);
        scrollPane.getViewport().addMouseMotionListener(dragMap);
        mapLabel.addMouseListener(dragMap);
        mapLabel.addMouseMotionListener(dragMap);

        buildingDropdown.addActionListener(e -> onBuildingSelected());
        floorDropdown.addActionListener(e -> onFloorSelected());

        loadMap(MAPS_DIR + File.separator + "General.png", "Campus Overview");
    }

    private void onBuildingSelected() {
        String selected = (String) buildingDropdown.getSelectedItem();
        floorDropdown.removeAllItems();

        if (selected == null || selected.startsWith("Select Building")) {
            floorDropdown.addItem("Select Floor");
            floorDropdown.setEnabled(false);
            showPlaceholder();
            return;
        }

        String code = BUILDINGS.get(selected);
        String[] floors = FLOORS.get(code);

        floorDropdown.addItem("Select Floor");
        for (String floor : floors) {
            floorDropdown.addItem(floor);
        }
        floorDropdown.setEnabled(true);
        showPlaceholder();
    }

    private void onFloorSelected() {
        String selectedBuilding = (String) buildingDropdown.getSelectedItem();
        String selectedFloor = (String) floorDropdown.getSelectedItem();

        if (selectedBuilding == null || selectedBuilding.startsWith("Select Building")
                || selectedFloor == null || selectedFloor.startsWith("Select Floor")) {
            return;
        }

        String buildingCode = BUILDINGS.get(selectedBuilding);
        String[] floorNames = FLOORS.get(buildingCode);
        String[] floorFiles = FLOOR_FILES.get(buildingCode);

        //Match display name to filename
        String fileSegment = null;
        for (int i = 0; i < floorNames.length; i++) {
            if (floorNames[i].equals(selectedFloor)) {
                fileSegment = floorFiles[i];
                break;
            }
        }

        if (fileSegment == null) return;

        String map = MAPS_DIR + File.separator + buildingCode + "_" + fileSegment + ".png";
        loadMap(map, selectedBuilding + " - " + selectedFloor);
    }

    //Loading the map images
    private void loadMap(String path, String description) {
        File file = new File(path);
        if (!file.exists()) {
            mapLabel.setIcon(null);
            mapLabel.setText("<html><center><i>Map not yet available:<br>" + path + "</i></center></html>");
            scrollPane.setViewportView(mapLabel);
            return;
        }

        ImageIcon icon = new ImageIcon(path);
        mapLabel.setIcon(icon);
        mapLabel.setText(null);
        mapLabel.setToolTipText(description);

        //Resize JLabel to image size so scroll works correctly
        mapLabel.setPreferredSize(
                new Dimension(icon.getIconWidth(),
                        icon.getIconHeight()));

        mapLabel.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));

        scrollPane.setViewportView(mapLabel);
        scrollPane.revalidate();
        scrollPane.repaint();

        SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, 0)));
    }

    private void showPlaceholder() {
        loadMap(MAPS_DIR + File.separator + "General.png", "Campus Overview");
    }
}
