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

### Application Setup

Ensure that either the latest JDK (JDK 25) is installed on your machine or your IDE of choice,
Git-Happens recommends IntelliJ IDEA is installed and functional

#### If using IntelliJ:
1. Clone in the repository using the Clone Repository Button on the opening page,
2. Go to File -> Project Structure and set the Project SDK to JDK 25
3. Open the Main file as the contains the main method
4. Click the green run arrow that appears at the top of the screen

#### If running the file from terminal:
1. Type `git clone https://github.com/Sean-Lildhar/Campus-Connect-CSCI2040U.git` into the terminal - Clones the Repo
2. Type `cd Campus-Connect-CSCI2040U` into terminal - Changes Directory into the repo
3. Type `javac -d out src/Main.java src/gui/*.java` into terminal - Compiles all the files
4. Type `java -cp src Main` into terminal - Runs the program

















