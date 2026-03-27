package model;

public class Waypoint {
    private final String id;

    public Waypoint(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBuilding() {
        String id = this.id;
        String building = id.substring(0, 2);
        return building;
    }

    public int getFloor() {
        String id = this.id;
        String floorString = id.substring(3, 3);
        try{
            int floor = Integer.parseInt(floorString);
            return floor;
        } catch (NumberFormatException e){
            System.err.println("Number Format Exception: " + e);
        }
        return 0;
    }
}