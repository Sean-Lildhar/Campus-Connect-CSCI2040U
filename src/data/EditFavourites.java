package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV format:
 * username,roomNumber,roomNumber,...
 */
public class EditFavourites {

    private final String FILE_PATH;

    public EditFavourites() {
        this.FILE_PATH = "data/favourites.csv";
    }

    public EditFavourites(String filePath) {
        this.FILE_PATH = filePath;
    }

    public List<String> getFavourites(String username) {
        for (String[] row : readAllRows()) {
            if (row.length >= 1 && row[0].equals(username)) {
                List<String> favs = new ArrayList<>();
                for (int i = 1; i < row.length; i++) {
                    String r = row[i].trim();
                    if (!r.isEmpty()) favs.add(r);
                }
                return favs;
            }
        }
        return new ArrayList<>();
    }

    public boolean addFavourite(String username, String roomNumber) {
        List<String> current = getFavourites(username);
        if (current.contains(roomNumber)) return false;
        current.add(roomNumber);
        writeFavourites(username, current);
        return true;
    }

    public void writeFavourites(String username, List<String> favourites) {
        List<String[]> allRows = readAllRows();
        boolean found = false;
        for (String[] row : allRows) {
            if (row.length >= 1 && row[0].equals(username)) {
                found = true;
                break;
            }
        }
        if (!found) {
            String[] newRow = new String[1 + favourites.size()];
            newRow[0] = username;
            for (int i = 0; i < favourites.size(); i++) newRow[i + 1] = favourites.get(i);
            allRows.add(newRow);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (String[] row : allRows) {
                if (row.length >= 1 && row[0].equals(username)) {
                    StringBuilder sb = new StringBuilder(username);
                    for (String fav : favourites) sb.append(",").append(fav);
                    pw.println(sb);
                } else {
                    pw.println(String.join(",", row));
                }
            }
        } catch (IOException e) {
            System.err.println("EditFavourites.writeFavourites error: " + e.getMessage());
        }
    }

    private List<String[]> readAllRows() {
        List<String[]> rows = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return rows;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) rows.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("EditFavourites.readAllRows error: " + e.getMessage());
        }
        return rows;
    }

}
