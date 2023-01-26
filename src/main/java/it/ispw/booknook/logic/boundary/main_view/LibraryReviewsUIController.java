package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.bean.ReviewBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.control.ReviewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LibraryReviewsUIController extends UIController implements Initializable {

    @FXML
    private Button addBookBtn;

    @FXML
    private BorderPane homePage;

    @FXML
    private ImageView medStar1;

    @FXML
    private ImageView medStar2;

    @FXML
    private ImageView medStar3;

    @FXML
    private ImageView medStar4;

    @FXML
    private ImageView medStar5;

    @FXML
    private VBox reviewContainer;

    @FXML
    private Button reviewsBtn;

    @FXML
    private AnchorPane rightBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoginController loginController = new LoginController();
        LoginBean loginDetails = loginController.getCurrentUsername();
        LibraryBean library = new LibraryBean();
        library.setUsername(loginDetails.getUsername());
        //Chiama ReviewController e carica le recensioni
        ReviewController reviewController = new ReviewController();
        List<ReviewBean> reviews = reviewController.getReviews(library);
        int sum  = 0;
        for (ReviewBean review : reviews) {
            //creare una cella nello scroll pane che mostra la review
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/review-cell.fxml"));
            try {
                Parent cell = fxmlLoader.load();
                reviewContainer.getChildren().add(cell);
                //settare i campi di cell
                Label usernameLabel = (Label)cell.lookup("#usernameLabel");
                usernameLabel.setText(review.getUsername());
                Label titleLabel = (Label)cell.lookup("#titleLabel");
                titleLabel.setText(review.getTitle());
                TextArea contentArea = (TextArea) cell.lookup("#contentArea");
                contentArea.setText(review.getText());
                Label dateLabel = (Label)cell.lookup("#dateLabel");
                dateLabel.setText(review.getDate());
                for (int i = 1; i <= review.getServiceRate(); i++) {
                    Node star = cell.lookup("#servStar" + i);
                    star.setVisible(true);
                }
                for (int i = 1; i <= review.getAvailabilityRate(); i++) {
                    Node star = cell.lookup("#avStar" + i);
                    star.setVisible(true);
                }
                for (int i = 1; i <= review.getLocationRate(); i++) {
                    Node star = cell.lookup("#locStar" + i);
                    star.setVisible(true);
                }
                for (int i = 1; i <= review.getMediumRate(); i++) {
                    Node star = cell.lookup("#medStar" + i);
                    star.setVisible(true);
                }
                sum += review.getMediumRate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int medium = 0;
        if (!reviews.isEmpty()) {
            medium = sum / reviews.size();
        }
        for (int i = 1; i <= medium; i++) {
            Node star = rightBox.lookup("#medStar" + i);
            star.setVisible(true);
        }
    }

    @FXML
    void onAddBookClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/librarian-home-view.fxml", event);
    }
}
