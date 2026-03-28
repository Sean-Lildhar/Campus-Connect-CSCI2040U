package model;

public class Waypoint {
    private final String id;
    private final String description;
    private final String building;
    private final int floor;

    public Waypoint(String id, String description, String building, int floor) {
        this.id = id;
        this.description = description;
        this.building = building;
        this.floor = floor;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getBuilding() { return building; }
    public int getFloor() { return floor; }
}