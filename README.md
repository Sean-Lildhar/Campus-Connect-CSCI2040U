# Campus Connect

A Java Swing desktop application providing a Google Maps-style campus navigation with a focus
on providing indoor directions.

The GitHub Project Board can be found here:
https://github.com/users/Sean-Lildhar/projects/3/views/1

The following is short video demonstrating how to set up and use the program:
https://drive.google.com/file/d/1vTki3hWLvy1NXp3h8S9N1GQ2lkOC6Clh/view?usp=sharing
---
## CSV Formats

users.csv : username,password,role(admin/user) <br>
locations.csv : roomNumber,locationType,status <br>
favourites.csv : username,roomNumber,roomNumber,... <br>
graph.csv : id,locationType,status,floor,neighbour,weight,neighbour,weight,... <br>
review.csv : roomNumber, username, Rating, ReviewText <br>
---
## Sample Credentials

Username Password Role <br>
admin admin admin <br>
user user user <br>
---
## Application Setup
#### Terminal:
1. Ensure that JDK21 or later is installed on your machine check by running `java -version` in your terminal
2. Type `git clone https://github.com/Sean-Lildhar/Campus-Connect-CSCI2040U.git` into the terminal - Clones the Repo
3. Type `cd Campus-Connect-CSCI2040U` into terminal - Changes Directory into the repo
4. Type `java -jar campus-connect.jar` into terminal
5. Use either the provided Sample Credentials or create your own

#### IntelliJ IDEA:
1. Clone the repository from the main menu `https://github.com/Sean-Lildhar/Campus-Connect-CSCI2040U.git`
2. Navigate to src -> Main
3. Open the Main file and click the Green play button that appears at the top of the screen
4. Use either the provided Sample Credentials or create your own

---
## Project Structure

```
Campus Connect/
    data/
        favourites.csv                      <- username,roomNumber,roomNumber
        graph.csv                           <- holds navigation logic
        locations.csv                       <- roomNumber,locationType,status
        reviews.csv                         <- holds review information
        users.csv                           <- username,password,role
    maps/                                   <- contains images used to show campus maps
    src/
        data/
            CampusGraph.java                <- loads data from graph.csv and shows directions
            DijkstraNavigationService.java  <- responsible for pathfinding
            EditFavourites.java             <- read/write favourites.csv
            EditLocation.java               <- read/write locations.csv
            EditReviews.java                <- adds and reads from reviews.csv
            EditUser.java                   <- read/write users.csv
            NavigationService.java          <- defines the navigation service
        gui/
            AdminScreen.java                <- admin room-status editor popup
            CreateAccountScreen.java        <- new-account popup
            FavouritesScreen.java           <- favourites list popup
            LoginFrame.java                 <- login screen
            MainFrame.java                  <- main window (JSplitPane)
            MapPanel.java                   <- map panel                    
            ReviewFrame.java                <- adding and reading review popup
            SearchScreen.java               <- location search results popup
            UserPerms.java                  <- allows admin to change user perms         
        model/
            Admin.java                      <- admin extends User
            Location.java                   <- campus location (room number, type, status) 
            RouteStep.java                  <- holds a signle navigation instruction
            User.java                       <- standard user (username, password, favourites)
            Waypoint.java                   <- indivudal node in the navigation graph
        Main.java
    test-data                               <- temporary directory made during testing
    tests/
        data/
            DijkstraNavigationTest.java     <- test the pathfinding algorithm  
            EditFavouritesTest.java         <- test the EditFavourites class
            EditLocationTest.java           <- test the EditLocation class
            EditReviewsTest.java            <- test the review feature
            EditUserTest.java               <- test the EditUser class
            NavigationTest.java             <- test the Navigation                
        model/
            AdminTest                       <- test the Admin class
            LocationTest                    <- test the Location class
            UserTest                        <- test the User class
    .gitignore                              <- files for git to ignore
    Campus-Connect.jar                      <- easily executable JAR file
    pom.xml                                 <- maven prokect configuration
    README.md                               <- project documentation
/                                
```

---

### Style Guide

This program follows the standard IntelliJ IDEA code format

















