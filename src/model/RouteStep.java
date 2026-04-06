package model;

public class RouteStep {
    private final String instruction;
    private final String waypointId;

    public RouteStep(String instruction, String waypointId) {
        this.instruction = instruction;
        this.waypointId = waypointId;
    }

    public String getInstruction() {
        return instruction;
    }

    @Override
    public String toString() {
        return instruction;
    }
}