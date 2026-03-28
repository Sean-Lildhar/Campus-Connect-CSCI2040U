package model;

public class Waypoint {
    private final String id;
    private final String description;
    private final String building;
    private final int floor;
    private final String locationType;

    public Waypoint(String id, String description, String building, int floor, String locationType) {
        this.id = id;
        this.description = description;
        this.building = building;
        this.floor = floor;
        this.locationType =  locationType;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getBuilding() { return building; }
    public int getFloor() { return floor; }
    public String getLocationType() { return locationType; }
}