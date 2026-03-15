package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a standard user.
 * Stores credentials and a list of favourite room numbers.
 */
public class User {

    protected String username;
    protected String password;
    private List<String> favouriteLocations;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.favouriteLocations = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getFavouriteLocations() {
        return favouriteLocations;
    }

    public void setFavouriteLocations(List<String> favs) {
        this.favouriteLocations = new ArrayList<>(favs);
    }


    //Adds a room number to favourites if not already present.

    public boolean addFavourite(String roomNumber) {
        if (!favouriteLocations.contains(roomNumber)) {
            favouriteLocations.add(roomNumber);
            return true;
        }
        return false;
    }

    public boolean isAdmin() {
        return false;
    }

    @Override
    public String toString() {
        return username + " (User)";
    }
}
