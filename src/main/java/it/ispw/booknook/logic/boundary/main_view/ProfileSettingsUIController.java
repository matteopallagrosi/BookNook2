package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.SettingsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileSettingsUIController extends UIController implements Initializable {

    @FXML
    private ImageView profilePicture;

    @FXML
    private ImageView avatar1;

    @FXML
    private ImageView avatar2;

    @FXML
    private ImageView avatar3;

    @FXML
    private ImageView avatar4;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField zipField;

    @FXML
    private TextField streetField;


    @FXML
    private Label updatedSettings;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SettingsController settingsController =  new SettingsController();
        LoginBean loginBean = settingsController.processProfileDetails();
        usernameField.setText(loginBean.getUsername());
        if ((loginBean.getFirstName() != null) && (loginBean.getLastName())!=null) {
            firstNameField.setText(loginBean.getFirstName());
            lastNameField.setText(loginBean.getLastName());
        }
        if ((loginBean.getAddress()) != null && (loginBean.getCity())!= null && (loginBean.getZip())!= null && (loginBean.getCountry())!= null) {
            streetField.setText(loginBean.getAddress());
            cityField.setText(loginBean.getCity());
            zipField.setText(loginBean.getZip());
            countryField.setText(loginBean.getCountry());
        }
        profilePicture.setImage(loginBean.getProfileImage());
    }

    @FXML
    void onChangePictureClick(ActionEvent event) {
        avatar1.setVisible(true);
        avatar2.setVisible(true);
        avatar3.setVisible(true);
        avatar4.setVisible(true);
    }

    @FXML
    void onFirstAvatarClick(MouseEvent event) {
        Image image = new Image("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_1.png");
        profilePicture.setImage(image);
        updatePicture(image);
    }

    @FXML
    void onSecondAvatarClick(MouseEvent event) {
        Image image = new Image("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_2.png");
        profilePicture.setImage(image);
        updatePicture(image);
    }

    @FXML
    void onThirdAvatarClick(MouseEvent event) {
        Image image = new Image("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_3.png");
        profilePicture.setImage(image);
        updatePicture(image);
    }

    @FXML
    void onFourthAvatarClick(MouseEvent event) {
        Image image = new Image("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_4.png");
        profilePicture.setImage(image);
        updatePicture(image);
    }

    @FXML
    void onUpdate(ActionEvent event) {
        LoginBean newProfile = new LoginBean();
        newProfile.setFirstName(firstNameField.getText());
        newProfile.setLastName(lastNameField.getText());
        newProfile.setAddress(streetField.getText());
        newProfile.setCity(cityField.getText());
        newProfile.setZip(zipField.getText());
        newProfile.setCountry(countryField.getText());
        //aggiorna nome e cognome
        SettingsController settingsController = new SettingsController();
        try {
            settingsController.changeProfileDetails(newProfile);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        updatedSettings.setVisible(true);

    }

   private void updatePicture(Image newImage) {
        LoginBean newImageProfile = new LoginBean();
        newImageProfile.setImage(newImage);
        SettingsController settingsController = new SettingsController();
        settingsController.updateImageProfile(newImageProfile);
   }

}
