package model;

public record RouteStep(String instruction, String waypointId) {

    @Override
    public String toString() {
        return instruction;
    }
}