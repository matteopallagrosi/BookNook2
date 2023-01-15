package it.ispw.booknook.logic.boundary;

import it.ispw.booknook.logic.bean.BookBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONManager implements Runnable {

    private BookBean book;

    public JSONManager(BookBean book) {
        this.book = book;
    }

    @Override
    public void run() {
        JSONObject bookJson = getJsonFromUrl();
        String url = getImageURL(bookJson);
        book.setCover(url);
    }

    public JSONObject getJsonFromUrl() {
        Properties properties = new Properties();
        String k = null;
        try (FileInputStream f = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\googleconfig.properties")) {
            properties.load(f);
            k = properties.getProperty("key");
        } catch (IOException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + book.getIsbn() + "&key=" + k;
        JSONObject myResponse = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            myResponse = new JSONObject(response.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return myResponse;
    }

    public String getImageURL(JSONObject book){
        String thumbnail = null;
        try {
            JSONArray itemsObject = book.getJSONArray("items");
            JSONObject bookInfo = itemsObject.getJSONObject(0);
            JSONObject volumeInfo = bookInfo.getJSONObject("volumeInfo");
            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            thumbnail = imageLinks.getString("smallThumbnail");
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return thumbnail;
    }
}
