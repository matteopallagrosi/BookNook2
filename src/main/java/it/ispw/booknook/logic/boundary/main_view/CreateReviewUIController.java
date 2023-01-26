package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.ReviewBean;
import it.ispw.booknook.logic.control.ReviewController;
import it.ispw.booknook.logic.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class CreateReviewUIController extends UIController {

    @FXML
    private ImageView locStar4;

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
    private ImageView backBtn;

    @FXML
    private Button clubsBtn;

    @FXML
    private Button consultBtn;

    @FXML
    private TextArea contentField;

    @FXML
    private Button discoverBtn;

    @FXML
    private BorderPane homePage;

    @FXML
    private ImageView locStar1;

    @FXML
    private ImageView locStar2;

    @FXML
    private ImageView locStar3;

    @FXML
    private ImageView locStar5;

    @FXML
    private Button myListBtn;

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
    private TextField titleField;

    @FXML
    private Label titleLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private AnchorPane container;

    @FXML
    private Label errorLabel;

    private static final String STAR_FULL = "C:\\Users\\HP\\IdeaProjects\\BookNook2\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\icons8-stella-riempita-30.png";
    private static final String STAR_EMPTY = "C:\\Users\\HP\\IdeaProjects\\BookNook2\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\icons8-stella-vuota-30.png";

    private String libraryUsername;
    private int servicesRate = 0;
    private int availabilityRate = 0;
    private int locationRate = 0;

    public void setLibraryDetails(LibraryBean bean) {
        libraryUsername = bean.getUsername();
        usernameLabel.setText(User.getUser().getUsername());
    }

    @FXML
    void onStarClick(MouseEvent event) {
        Node star = (Node) event.getSource();
        String starName = star.getId();
        char starSelected = starName.charAt(starName.length()-1);
        //numero di stelle selezionato
        int starNumber = Character.getNumericValue(starSelected);
        char []type = new char[starName.length()-1];
        starName.getChars(0, starName.length()-1, type, 0);
        setNumberStars(String.valueOf(type), starNumber);
        switch (String.valueOf(type)) {
            case "servStar" -> servicesRate = starNumber;
            case "avStar" -> availabilityRate = starNumber;
            case "locStar" -> locationRate = starNumber;
            default -> servicesRate = starNumber;
        }
    }

    private void setNumberStars(String type, int n) {
        ///svuota tutte le stelle
        for (int i = 1; i <= 5; i++) {
            ImageView star = (ImageView) container.lookup("#" + type + i);
            star.setImage(new Image(STAR_EMPTY));
        }
        //colora il numero corretto di stelle
        for (int i = 1; i <= n; i++) {
            ImageView star = (ImageView) container.lookup("#" + type + i);
            star.setImage(new Image(STAR_FULL));
        }
    }

    @FXML
    void onSubmitClick(ActionEvent event) {
        errorLabel.setVisible(false);
        //verifica che tutti i campi siano stati riempiti
        if (servicesRate == 0 || availabilityRate == 0 || locationRate == 0 || titleField.getText().isEmpty() || contentField.getText().isEmpty() ) {
            errorLabel.setVisible(true);
        }
        else {
            //salva la recensione
            ReviewController reviewController = new ReviewController();
            ReviewBean newReview = new ReviewBean();
            newReview.setUsername(User.getUser().getUsername());
            newReview.setTitle(titleField.getText());
            newReview.setText(contentField.getText());
            newReview.setServiceRate(servicesRate);
            newReview.setAvailabilityRate(availabilityRate);
            newReview.setLocationRate(locationRate);
            LibraryBean library = new LibraryBean();
            library.setUsername(libraryUsername);
            reviewController.addReview(newReview, library);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/rate-view.fxml"));
                Parent root = loader.load();
                ReviewUIController controller = loader.getController();
                controller.setReviews(library);
                Scene scene = ((Node)(event.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}