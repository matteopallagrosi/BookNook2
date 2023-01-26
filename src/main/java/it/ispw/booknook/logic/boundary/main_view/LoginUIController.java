package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.io.IOException;


public class LoginUIController extends UIController {

    @FXML
    private TextField emailTf;

    @FXML
    private PasswordField passwTf;

    @FXML
    private Label errorField;

    @FXML
    private Rectangle errorPanel;

    @FXML
    void onLoginClick(ActionEvent event) throws IOException {
        //recupera email e password
        //crea loginBean
        LoginBean loginB = new LoginBean(emailTf.getText(), passwTf.getText());
        //verifica email e password inseriti su database
        LoginController controller = new LoginController();
        //se corretti verifica tipo utente
        if (controller.checkUserLogged(loginB)) {
            //se utente lettore apre homepage per reader
            if (controller.isUserReader()) {
                //apre hompepage
                changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
            }
            //se utente bibliotecario apre interfaccia bibliotecario
            else {
                changePage("/it/ispw/booknook/mainView/librarian-home-view.fxml", event);
            }
        }
        //altrimenti mostra messaggio d'errore
        else {
            errorPanel.setVisible(true);
            errorField.setVisible(true);
        }
    }

    @FXML
    void onSignupClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/signup-view.fxml", event);
    }

    @FXML
    void onContinueNoAccount(ActionEvent event) throws IOException {
        //apre direttamente hompepage
        changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
    }

}