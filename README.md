# Campus Navigator

A Java Swing desktop application providing the foundation for a Google Maps–style
campus navigation system. Navigation and pathfinding are **not yet implemented** —
this release focuses on login, user management, location data, and the GUI framework.

The GitHub Project Board can be found here:
https://github.com/users/Sean-Lildhar/projects/3/views/1

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
├── src/
│   ├── Main.java                       
│   ├── model/
│   │   ├── User.java                   <- standard user (username, password, favourites)
│   │   ├── Admin.java                  <- admin extends User
│   │   └── Location.java               <- campus location (room, type, status)
│   ├── data/
│   │   ├── UserDAO.java                <- read/write users.csv
│   │   ├── LocationDAO.java            <- read/write locations.csv
│   │   └── FavouritesDAO.java          <- read/write favourites.csv
│   └── gui/
│       ├── LoginFrame.java             <- login screen
│       ├── MainFrame.java              <- main window (JSplitPane)
│       ├── CreateAccountDialog.java    <- new-account popup
│       ├── SearchDialog.java           <- location search results popup
│       ├── FavouritesDialog.java       <- favourites list popup
│       └── AdminRoomEditorDialog.java  <- admin room-status editor popup
├── data/
│   ├── users.csv                       <- username,password,role
│   ├── locations.csv                   <- roomNumber,locationType,status
│   └── favourites.csv                  <- username,roomNumber,...
├── out/                                
```



### Project-CSCI2040U
This is the GitHub Repository for the Software Design and Analysis (CSCI-2040U) Term Project.
The repo has private access so everything here is just between us.

### Style Guide
We will be following the standard IntelliJ IDEA code format

To keep the code consistent we should open a new branch every time we go to solve a new issue that was on the project board and then merge the branch to main once the code has been checked.

Any functions we create will have a comment saying which requirement it fulfils to keep everything organized.

### Contract
https://docs.google.com/document/d/1VipmW8vkt5_UCaz_loQ3VfByI6DXn7l5zuI0lDceEp0/edit?tab=t.0#heading=h.8vounpy78p60

