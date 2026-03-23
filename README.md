# Campus Connect

A Java Swing desktop application providing a Google Maps-style campus navigation with a focus
on providing indoor directions.

This iteration focuses on Account Creation (CreateAccountScreen),
Login Functionality (LoginFrame), the Main Application Screens (MainFrame),
Admin Functionality (AdminScreen), User Permissions Management (UserPerms) and
Favourites System (FavouritesScreen). Navigation is not yet implemented.

The GitHub Project Board can be found here:
https://github.com/users/Sean-Lildhar/projects/3/views/1
---

## CSV Formats

users.csv : username,password,role(admin/user) <br>
locations.csv : roomNumber,locationType,status <br>
favourites.csv : username,roomNumber,roomNumber,... <br>

### Sample credentials

Username Password Role <br>
admin admin admin <br>
user user user <br>

---

## Project Structure

```
Campus Connect/
    data/
        users.csv                           <- username,password,role
        locations.csv                       <- roomNumber,locationType,status
        favourites.csv                      <- username,roomNumber,...
    maps/                                   <- contains imaages used to show campus maps
    META_INF/
        MANIFEST.MF                         <- used in creation of JAR file
    out/
        production/
        Campus-Connect-CSCI2040U/
            META_INF/
                MANIFEST.MF                 
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
        META-INF/
            MANIFEST.MF
        model/
            User.java                       <- standard user (username, password, favourites)
            Admin.java                      <- admin extends User
            Location.java                   <- campus location (room number, type, status)
        Main.java
    test-data                               <- temporary directory made during testing
    tests
        data
            EditFavouritesTest              <- test the EditFavourites class
            EditLocationTest                <- test the EditLocation class
            EditUserTest                    <- test the EditUser class
        model
            AdminTest                       <- test the Admin class
            LocationTest                    <- test the Location class
            UserTest                        <- test the User class
    .gitignore
    Campus-Connect-CSCI2040U.iml
    Campus-Connect.jar                      <- easily executable JAR file
    README.md
/                                
```

---

### Style Guide

We will be following the standard IntelliJ IDEA code format

### Application Setup
#### Terminal:
1. Ensure that JDK21 or later is installed on your machine check by running `java -version` in your terminal 
2. Type `git clone https://github.com/Sean-Lildhar/Campus-Connect-CSCI2040U.git` into the terminal - Clones the Repo
3. Type `cd Campus-Connect-CSCI2040U` into terminal - Changes Directory into the repo
4. Type `java -jar Campus-Connect.jar` into terminal
5. Use either the provided Sample Credentials or create your own

#### IntelliJ IDEA:
1. Clone the repository from the main menu `https://github.com/Sean-Lildhar/Campus-Connect-CSCI2040U.git`
2. Navigate to src -> Main
3. Open the Main file and click the Green play button that appears at the top of the screen 
4. Use either the provided Sample Credentials or create your own

















