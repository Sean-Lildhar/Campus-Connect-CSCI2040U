# Campus Navigator

A Java Swing desktop application providing a Google Maps-style campus navigation with a .
This iteration focuses on login, user management, location data, and the GUI framework.
Navigation is not yet implemented.

The GitHub Project Board can be found here:
https://github.com/users/Sean-Lildhar/projects/3/views/1
---
## CSV Formats

users.csv : username,password,role(admin/user) <br>
locations.csv : roomNumber,locationType,status <br>
favourites.csv : username,roomNumber,roomNumber,... <br>

### Sample credentials
Username    Password    Role <br>
admin       admin       admin <br>
user        user        user <br>

---

## Project Structure

```
Campus Connect/
    data/
        users.csv                           <- username,password,role
        locations.csv                       <- roomNumber,locationType,status
        favourites.csv                      <- username,roomNumber,...
    src/
        data/
            EditUser.java                   <- read/write users.csv
            EditLocation.java               <- read/write locations.csv
            EditFavourites.java             <- read/write favourites.csv
        gui/
            AdminScreen.java                <- admin room-status editor popup
            CreateAccountScreen.java        <- new-account popup
            FavouritesScreen.java           <- favourites list popup
            LoginFrame.java                 <- login screen
            MainFrame.java                  <- main window (JSplitPane)
            SearchScreen.java               <- location search results popup
            UserPerms.java                  <- allows admin to change user perms
        model/
            User.java                       <- standard user (username, password, favourites)
            Admin.java                      <- admin extends User
            Location.java                   <- campus location (room number, type, status)
    Main.java
/                                
```
---

### Style Guide
We will be following the standard IntelliJ IDEA code format

### Contract
https://docs.google.com/document/d/1VipmW8vkt5_UCaz_loQ3VfByI6DXn7l5zuI0lDceEp0/edit?tab=t.0#heading=h.8vounpy78p60

