package it.ispw.booknook.logic.database.dao;

import it.ispw.booknook.logic.entity.Review;

import java.io.*;
import java.util.ArrayList;

public class ReviewDao {

    //deserializzazione
    public static ArrayList<Review> getReviews(String usernameLibrary) {
        ArrayList<Review> reviews = null;
        try(FileInputStream f = new FileInputStream("src/main/resources/" + usernameLibrary + ".ser");
            ObjectInputStream s = new ObjectInputStream(f)) {
            reviews = (ArrayList<Review>)s.readObject();
        }
        catch (EOFException e ) {
            return reviews;
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    //serializzazione di una nuova recensione
    public static void addReview(Review review, String usernameLibrary) {
        ArrayList<Review> reviews = getReviews(usernameLibrary);
        if (reviews == null) reviews = new ArrayList<Review>();
        reviews.add(0, review);
        try (FileOutputStream f = new FileOutputStream("src/main/resources/" + usernameLibrary + ".ser");
             ObjectOutputStream s = new ObjectOutputStream(f)) {
            s.writeObject(reviews);
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}
