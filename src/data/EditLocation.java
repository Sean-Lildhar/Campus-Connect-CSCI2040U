package data;

import model.Location;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV format:
 * roomNumber,locationType,status
 * locationType: restaurant | washroom | water fountain | classroom
 * status: open or closed
 */
public class EditLocation {

    private final String FILE_PATH;

    public EditLocation() {
        this.FILE_PATH = "data/locations.csv";
    }

    public EditLocation(String filePath) {
        this.FILE_PATH = filePath;
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return locations;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // Split into at most 3 parts so "water fountain" is preserved
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    locations.add(new Location(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("EditLocation.getAllLocations error: " + e.getMessage());
        }
        return locations;
    }

    public List<Location> getLocationsByType(String type) {
        List<Location> result = new ArrayList<>();
        for (Location loc : getAllLocations()) {
            if (loc.getLocationType().equalsIgnoreCase(type)) {
                result.add(loc);
            }
        }
        return result;
    }

    public Location getLocationByRoom(String roomNumber) {
        for (Location loc : getAllLocations()) {
            if (loc.getRoomNumber().equals(roomNumber)) return loc;
        }
        return null;
    }

    public void updateLocation(Location updated) {
        List<Location> all = getAllLocations();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Location loc : all) {
                if (loc.getRoomNumber().equals(updated.getRoomNumber())) {
                    pw.println(updated.getRoomNumber() + ","
                            + updated.getLocationType() + ","
                            + updated.getStatus());
                } else {
                    pw.println(loc.getRoomNumber() + ","
                            + loc.getLocationType() + ","
                            + loc.getStatus());
                }
            }
        } catch (IOException e) {
            System.err.println("EditLocation.updateLocation error: " + e.getMessage());
        }
    }
}
