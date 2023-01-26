package it.ispw.booknook.logic.boundary.main_view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClubsUIController extends UIController implements Initializable {
    @FXML
    private BorderPane currentPane;

    @FXML
    private Label createClubBtn;

    @FXML
    private Rectangle myClubsBtn;

    @FXML
    private Rectangle mapBtn;

    @FXML
    private Rectangle topicBtn;

    private static final String SELECTED_COLOR = "#e9bf8e";
    private static final String DEFAULT_COLOR = "#8a8a8a66";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane topicPane = null;
        try {
            topicPane = FXMLLoader.load(getClass().getResource("/it/ispw/booknook/mainView/topics-view.fxml"));
        } catch (IOException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
        currentPane.setCenter(topicPane);
        setAvatar();
    }

    @FXML
    void onBackClick(MouseEvent event) {

    }

    @FXML
    void onCreateClubClick(MouseEvent event) {

    }


    @FXML
    void onMapClick(MouseEvent event) throws IOException {
        mapBtn.setFill(Color.web(SELECTED_COLOR));
        topicBtn.setFill(Color.web(DEFAULT_COLOR));
        myClubsBtn.setFill((Color.web(DEFAULT_COLOR)));
        AnchorPane mapPane = FXMLLoader.load(getClass().getResource("/it/ispw/booknook/mainView/clubsMap-view.fxml"));
        currentPane.setCenter(mapPane);
    }

    @FXML
    void onMyClubsClick(MouseEvent event) throws IOException {
        mapBtn.setFill(Color.web(DEFAULT_COLOR));
        topicBtn.setFill(Color.web(DEFAULT_COLOR));
        myClubsBtn.setFill((Color.web(SELECTED_COLOR)));
        AnchorPane mapPane = FXMLLoader.load(getClass().getResource("/it/ispw/booknook/mainView/myClubs-view.fxml"));
        currentPane.setCenter(mapPane);

    }

    @FXML
    void onTopicsClick(MouseEvent event) throws IOException {
        mapBtn.setFill(Color.web(DEFAULT_COLOR));
        topicBtn.setFill(Color.web(SELECTED_COLOR));
        myClubsBtn.setFill((Color.web(DEFAULT_COLOR)));
        AnchorPane topicPane = FXMLLoader.load(getClass().getResource("/it/ispw/booknook/mainView/topics-view.fxml"));
        currentPane.setCenter(topicPane);
    }
}
