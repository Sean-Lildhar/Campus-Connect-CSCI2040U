package data;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EditReviews {
    private final String filepath = "data/review.csv";

    public void addReview(String user, String room, int rating, String text){
        try(PrintWriter pw = new PrintWriter(new FileWriter(filepath, true))){
            pw.println(user + "," + room + "," + rating + "," + text);
        } catch (IOException e){
            System.err.println("Error leaving a review");
        }
    }

    public List<String> getRoomReviews(String room){
        List<String> reviews = new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))){
            String line;
            while((line = bufferedReader.readLine()) != null){
                String cells[] = line.split(",", 4);
                if(cells[1].equals(room)){
                    reviews.add(cells[0] + " (" + cells[2] + " Star(s)): " + cells[3]);
                }
            }
        } catch(IOException e){
            System.err.println("Error loading reviews");
        }
        return reviews;
    }
}
