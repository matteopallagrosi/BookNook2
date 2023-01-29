package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.control.SettingsController;
import it.ispw.booknook.logic.exception.UserNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class AccountSettingsUIController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPwdField;

    @FXML
    private PasswordField oldPwdField;

    @FXML
    private Button updateBtn;

    @FXML
    private Label errorLabel;

    @FXML
    private Label pwdError;

    @FXML
    private Label newPwdError;

    @FXML
    private Label emailUpdated;

    @FXML
    private Label pwdUpdated;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SettingsController controller = new SettingsController();
        LoginBean loginDetails = controller.processUserDetails();
        emailField.setText(loginDetails.getEmail());
    }


    @FXML
    void changeDetails(ActionEvent event) {
        emailUpdated.setVisible(false);
        pwdUpdated.setVisible(false);
        //l'utente ha cambiato email
        SettingsController controller = new SettingsController();
        LoginController loginController = new LoginController();
        LoginBean oldDetails = controller.processUserDetails();
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(emailField.getText());
        if (loginBean.getEmail() == null) {  //validazione sintattica
            errorLabel.setVisible(true);
            return;
        }
        errorLabel.setVisible(false);
        //inserisce nuova email nel db solo se è cambiata

        controller.changeEmail(oldDetails, loginBean);


        //aggiornare email utente loggato
        loginController.updateUserEmail(loginBean);
        emailUpdated.setVisible(true);

        if (!oldPwdField.getText().isEmpty()) {
            oldDetails.setPlainPassword(oldPwdField.getText());
            try {
            //se old password è sbagliata mostra errore
            loginController.checkUserLogged(oldDetails);
            } catch (UserNotFoundException e) {
                pwdError.setVisible(true);
                return;
            }

            pwdError.setVisible(false);
            //altrimenti valida sintatticamente la nuova password
            loginBean.setPassword(newPwdField.getText());
            if (loginBean.getPassword() == null) {
                newPwdError.setVisible(true);
                return;
            }
            newPwdError.setVisible(false);
            //inserisce la nuova email e password nel db

            controller.changePassword(loginBean);


            pwdUpdated.setVisible(true);
        }
    }

}
