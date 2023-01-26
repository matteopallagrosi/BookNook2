package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.ReviewBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.control.ReviewController;
import it.ispw.booknook.logic.entity.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReviewUIController extends UIController {

    @FXML
    private ImageView backBtn;

    @FXML
    private Button clubsBtn;

    @FXML
    private Button consultBtn;

    @FXML
    private Button discoverBtn;

    @FXML
    private BorderPane homePage;

    @FXML
    private Button myListBtn;

    @FXML
    private ImageView profileBtn;

    @FXML
    private VBox reviewContainer;

    @FXML
    private ImageView avStar1;

    @FXML
    private ImageView avStar2;

    @FXML
    private ImageView avStar3;

    @FXML
    private ImageView avStar4;

    @FXML
    private ImageView avStar5;

    @FXML
    private Label availabilityLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView locStar1;

    @FXML
    private ImageView locStar2;

    @FXML
    private ImageView locStar3;

    @FXML
    private ImageView locStar4;

    @FXML
    private ImageView locStar5;

    @FXML
    private Label locationLabel;

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
    private ImageView servStar1;

    @FXML
    private ImageView servStar2;

    @FXML
    private ImageView servStar3;

    @FXML
    private ImageView servStar4;

    @FXML
    private ImageView servStar5;

    @FXML
    private Label servicesLabel;

    @FXML
    private Button newReviewBtn;

    @FXML
    private AnchorPane rightBox;

    LibraryBean currentLibrary;

    public void setReviews(LibraryBean library) {
        setAvatar();
        currentLibrary = library;
        //Chiama ReviewController e carica le recensioni
        ReviewController reviewController = new ReviewController();
        ArrayList<ReviewBean> reviews = reviewController.getReviews(library);
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
        if (reviews.size() != 0) {
            medium = sum / reviews.size();
        }
        for (int i = 1; i <= medium; i++) {
            Node star = rightBox.lookup("#medStar" + i);
            star.setVisible(true);
        }
    }

    @FXML
    void onNewReviewClick(ActionEvent event) throws IOException {
        //verifica login
        LoginController loginController = new LoginController();
        if (!loginController.verifyLogin()) {
            //apre un dialog per login/signup
            DialogController dialogController = new DialogController();
            dialogController.createLoginDialog();
        }
        //se l'utente è loggato il libro è aggiunto alla lista scelta;
        if (loginController.verifyLogin()) {
            //aprire pagina nuova recensione
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/newreview-view.fxml"));
                Parent root = loader.load();
                CreateReviewUIController controller = loader.getController();
                controller.setLibraryDetails(currentLibrary);
                Scene scene = ((Node) (event.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
