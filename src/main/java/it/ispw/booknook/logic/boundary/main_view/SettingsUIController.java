package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LoginBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class SettingsUIController extends UIController implements Initializable {

    @FXML
    private BorderPane currentSettingsPane;

    @FXML
    private Rectangle accountBtn;

    @FXML
    private Rectangle profSettingsBtn;

    @FXML
    private Rectangle deleteBtn;

    LoginBean profileDetails;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            AnchorPane accountPane = FXMLLoader.load(getClass().getResource("/it/ispw/booknook/mainView/accountsettings-view.fxml"));
            currentSettingsPane.setCenter(accountPane);
        } catch (IOException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
        //carica i dati (quelli presenti) dell'utente
        setAvatar();
        profileDetails = new LoginBean();
        //passa al bean il riferimento all'immage view, il bean viene notificato
        //del cambiamento dell'immagine profilo e la cambia
        profileDetails.setAvatarImage(profileBtn);
    }

    @FXML
    void onAccountSettingsClick(MouseEvent event) throws IOException {
        accountBtn.setFill(Color.web("#e9bf8e"));
        profSettingsBtn.setFill(Color.web("#8a8a8a66"));
        AnchorPane accountPane = FXMLLoader.load(getClass().getResource("/it/ispw/booknook/mainView/accountsettings-view.fxml"));
        currentSettingsPane.setCenter(accountPane);
    }

    @FXML
    void onProfileSettingsClick(MouseEvent event) throws IOException {
        profSettingsBtn.setFill(Color.web("#e9bf8e"));
        accountBtn.setFill(Color.web("#8a8a8a66"));
        AnchorPane profilePane = FXMLLoader.load(getClass().getResource("/it/ispw/booknook/mainView/profile-settings-view.fxml"));
        currentSettingsPane.setCenter(profilePane);
    }

    @FXML
    void onDeleteClick(MouseEvent event) {
        DialogController controller = new DialogController();
        controller.createDeleteDialog(event);
    }
}
