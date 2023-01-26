package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.control.SettingsController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class UIController {

    @FXML
    protected ImageView profileBtn;

    protected void changePage(String page, Event event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page)));
        Scene scene = ((Node)(event.getSource())).getScene();
        scene.setRoot(root);
        root.requestFocus();
    }

    @FXML
    protected void onProfileClick(MouseEvent event) throws IOException {
        //prima verifica che l'utente sia loggato
        LoginController loginController = new LoginController();
        if (!loginController.verifyLogin()) {
            //apre un dialog per login/signup
            DialogController dialogController = new DialogController();
            dialogController.createLoginDialog();
        }
        //se l'utente è loggato il libro è aggiunto alla lista scelta;
        if (loginController.verifyLogin()) {
            changePage("/it/ispw/booknook/mainView/settings-view.fxml", event);
        }
    }

    @FXML
    protected void onMyListClick(Event event) throws IOException {
        //prima verifica che l'utente sia loggato
        LoginController loginController = new LoginController();
        if (!loginController.verifyLogin()) {
            //apre un dialog per login/signup
            DialogController dialogController = new DialogController();
            dialogController.createLoginDialog();
        }
        //se l'utente è loggato il libro è aggiunto alla lista scelta;
        if (loginController.verifyLogin()) {
            changePage("/it/ispw/booknook/mainView/myLists-view.fxml", event);
        }

    }

    @FXML
    void onDiscoverClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
    }

    @FXML
    void onConsultationClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/consultation-view.fxml", event);
    }

    @FXML
    void onClubsClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/clubs-view.fxml", event);
    }

    protected void setAvatar() {
        SettingsController settingsController = new SettingsController();
        LoginBean profileDetails = settingsController.processProfileDetails();
        //se l'utente non è loggato non è presente un immagine di profilo
        if (profileDetails.getProfileImage()!=null) {
            profileBtn.setImage(profileDetails.getProfileImage());
        }
    }
}
