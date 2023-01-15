package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.SignUpController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Objects;

public class SignupUIController extends UIController {

    @FXML
    private TextField usernameTf;

    @FXML
    private TextField emailTf;

    @FXML
    private PasswordField passwordTf;

    @FXML
    private Rectangle errorPanel;

    @FXML
    private Label emailErrorField;

    @FXML
    private Label passwordErrorField;

    @FXML
    private Label usernameErrorField;

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

        //se corrette registra utente (inserisce dati sul db), chiamando controller applicativo
        SignUpController controller = new SignUpController();
        controller.registerReader(loginBean);


        //aprire messaggio registrazione con successo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration succeded");
        alert.setHeaderText(null);
        alert.setContentText("Your registration has been completed successfully. Have fun!");
        alert.showAndWait();

       //alla chiusura del dialog apre l'homepage
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/it/ispw/booknook/mainView/homepage-view.fxml")));
        Scene scene = ((Button)(event.getSource())).getScene();
        scene.setRoot(root);
        root.requestFocus();

    }

    private void showEmailError() {
        errorPanel.setVisible(true);
        passwordErrorField.setVisible(false);
        usernameErrorField.setVisible(false);
        emailErrorField.setVisible(true);
    }

    private void showPasswordError() {
        errorPanel.setVisible(true);
        emailErrorField.setVisible(false);
        usernameErrorField.setVisible(false);
        passwordErrorField.setVisible(true);
    }

    private void showUsernameError() {
        errorPanel.setVisible(true);
        emailErrorField.setVisible(false);
        usernameErrorField.setVisible(true);
        passwordErrorField.setVisible(false);
    }


    @FXML
    void onSigninClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/it/ispw/booknook/mainView/login-view.fxml")));
        Scene scene = ((Button)(event.getSource())).getScene();
        scene.setRoot(root);
        root.requestFocus();
    }

    public void setErrorMessage() {

    }

    public void registerUser() {

    }

    @FXML
    void onContinueNoAccount(ActionEvent event) throws IOException {
        //apre direttamente hompepage
        changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
    }
}
