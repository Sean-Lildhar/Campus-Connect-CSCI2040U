package data;

import model.RouteStep;
import java.util.List;

public interface NavigationService {
//returns the steps
    List<RouteStep> getDirections(String startRoom, String endRoom);
}