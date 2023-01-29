package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.entity.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MyClubsUIController extends UIController implements Initializable {

    @FXML
    private ListView<Club> clubsList;

    private static final String CLUB_TITLE = "A wonderful book club ";
    private static final long NUM_MEMBERS = 20500L;
    private static final String CLUB_DESCRIPTION = "Keeping romance, history, humor, mystery, love, intrigue, and passion interesting and fun. We discuss everything from reading to music to playing games to what makes a book good!!";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAvatar();
        setListView();
    }

    public void setListView()
    {
        Club club1 = new Club(CLUB_TITLE, NUM_MEMBERS, CLUB_DESCRIPTION);
        Club club2 = new Club(CLUB_TITLE, NUM_MEMBERS, CLUB_DESCRIPTION);
        Club club3 = new Club(CLUB_TITLE, NUM_MEMBERS, CLUB_DESCRIPTION);
        Club club4 = new Club(CLUB_TITLE, NUM_MEMBERS, CLUB_DESCRIPTION);
        ObservableList<Club> observableList = FXCollections.observableArrayList(club1, club2, club3, club4);
        clubsList.setItems(observableList);
        clubsList.setStyle("-fx-focus-color: transparent;");
        clubsList.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/ispw/booknook/mainView/bookList.css")).toExternalForm());
        clubsList.setFocusTraversable(false);
        clubsList.setCellFactory(listView -> new ClubViewCell());
    }
}
