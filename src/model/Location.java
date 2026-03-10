package model;

/**
 * Fields map directly to the locations CSV: roomNumber, locationType, status
 */
public class Location {

    private final String roomNumber;
    private final String locationType;   // restaurant | washroom | water fountain | classroom
    private String status;         // open | closed

    public Location(String roomNumber, String locationType, String status) {
        this.roomNumber = roomNumber;
        this.locationType = locationType;
        this.status = status;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + locationType + ")  —  " + status;
    }
}
