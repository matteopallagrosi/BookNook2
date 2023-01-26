package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.SignUpController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LibrarianSignUIController extends UIController implements Initializable {
    @FXML
    private TextField usernameTf;

    @FXML
    private TextField emailTf;

    @FXML
    private PasswordField passwordTf;

    @FXML
    private Rectangle errorPanel;
    @FXML
    private Rectangle errorPanel2;

    @FXML
    private Label emailErrorField;

    @FXML
    private Label passwordErrorField;

    @FXML
    private Label usernameErrorField;

    @FXML
    private TextField nameTf;

    @FXML
    private TextField addressTf;

    @FXML
    private TextField cityTf;

    @FXML
    private Label missingLabel;

    @FXML
    private ComboBox<String> hourOpen;

    @FXML
    private ComboBox<String> minOpen;

    @FXML
    private ComboBox<String> hourClose;

    @FXML
    private ComboBox<String> minClose;

    @FXML
    private Label hoursError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            }
            else {
                hours.add(String.valueOf(i));
            }
        }
        hourOpen.getItems().addAll(hours);
        hourClose.getItems().addAll(hours);

        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes.add("0" + i);
            }
            else {
                minutes.add(String.valueOf(i));
            }
        }
        minOpen.getItems().addAll(minutes);
        minClose.getItems().addAll(minutes);
    }

    @FXML
    void onSignUpClick(ActionEvent event) throws IOException {
        //recuperare email e password inserite
        //creare il bean che esegue controllo sintattico email e password
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(emailTf.getText());
        loginBean.setUsername(usernameTf.getText());
        if (loginBean.getUsername().isEmpty()) {
            showUsernameError();
            return;
        }
        if (loginBean.getEmail() == null) {
            showEmailError();
            return;
        }
        loginBean.setPassword(passwordTf.getText());
        if (loginBean.getPassword() == null) {
            showPasswordError();
            return;
        }
        loginBean.setIsReader(false);

        if (nameTf.getText().isEmpty() || addressTf.getText().isEmpty() || cityTf.getText().isEmpty()
               || hourOpen.getValue() == null || minOpen.getValue() == null || hourClose.getValue() == null || minClose.getValue() == null) {
            showMissingError();
            return;
        }

        LibraryBean libraryDetails = new LibraryBean();
        libraryDetails.setUsername(usernameTf.getText());
        libraryDetails.setName(nameTf.getText());
        libraryDetails.setAddress(addressTf.getText());
        libraryDetails.setCity(cityTf.getText());
        libraryDetails.setOpeningTime(hourOpen.getValue() + ":" + minOpen.getValue());
        libraryDetails.setClosingTime(hourClose.getValue() + ":" + minClose.getValue());

        //se orario chiusura precedente a orario apertura mostra msg errore
        if (libraryDetails.closing().toLocalTime().isBefore(libraryDetails.opening().toLocalTime())) {
            showHoursError();
            return;
        }

        //se corrette registra utente (inserisce dati sul db), chiamando controller applicativo
        SignUpController controller = new SignUpController();
        controller.registerUser(loginBean);

        //invoca controller per registrazione dati libreria
        controller.registerLibrary(libraryDetails);

        errorPanel2.setVisible(false);
        missingLabel.setVisible(false);
        errorPanel.setVisible(false);
        passwordErrorField.setVisible(false);
        usernameErrorField.setVisible(false);
        emailErrorField.setVisible(false);

        //aprire messaggio registrazione con successo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration succeded");
        alert.setHeaderText(null);
        alert.setContentText("Your registration has been completed successfully. Have fun!");
        alert.showAndWait();

        //alla chiusura del dialog apre l'homepage del bibliotecario
        changePage("/it/ispw/booknook/mainView/librarian-home-view.fxml", event);
    }

    private void showEmailError() {
        errorPanel2.setVisible(false);
        missingLabel.setVisible(false);
        errorPanel.setVisible(true);
        passwordErrorField.setVisible(false);
        usernameErrorField.setVisible(false);
        emailErrorField.setVisible(true);
    }

    private void showPasswordError() {
        errorPanel2.setVisible(false);
        missingLabel.setVisible(false);
        errorPanel.setVisible(true);
        emailErrorField.setVisible(false);
        usernameErrorField.setVisible(false);
        passwordErrorField.setVisible(true);
    }

    private void showUsernameError() {
        errorPanel2.setVisible(false);
        missingLabel.setVisible(false);
        errorPanel.setVisible(true);
        emailErrorField.setVisible(false);
        usernameErrorField.setVisible(true);
        passwordErrorField.setVisible(false);
    }

    private void showMissingError() {
        errorPanel.setVisible(false);
        passwordErrorField.setVisible(false);
        usernameErrorField.setVisible(false);
        emailErrorField.setVisible(false);
        errorPanel2.setVisible(true);
        missingLabel.setVisible(true);
    }

    private void showHoursError() {
        errorPanel.setVisible(false);
        passwordErrorField.setVisible(false);
        usernameErrorField.setVisible(false);
        emailErrorField.setVisible(false);
        errorPanel2.setVisible(true);
        missingLabel.setVisible(false);
        hoursError.setVisible(true);
    }

    @FXML
    void onReaderClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/signup-view.fxml", event);
    }

    @FXML
    void onSigninClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/login-view.fxml", event);
    }
}
