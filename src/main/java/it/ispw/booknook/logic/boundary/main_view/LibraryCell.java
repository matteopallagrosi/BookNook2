package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.control.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class LibraryCell {
    @FXML
    private Button borrowBtn;

    @FXML
    private Label name;

    @FXML
    private AnchorPane cell;

    public LibraryCell()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/libraryListItem.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void setBtnHandler(LibraryBean library) {
        borrowBtn.setOnAction(actionEvent -> {
            DialogController dialogController = new DialogController();
            //verifica se l'utente è loggato oppure no
            LoginController loginController = new LoginController();
            //se l'utente non è loggato apre un dialog per login/signup
            if (!loginController.verifyLogin()) {
                //apre un dialog per login/signup
                dialogController.createLoginDialog();
            }

            if (loginController.verifyLogin() && borrowBtn.getText().equals("Borrow")) {
                dialogController.createBorrowDialog(actionEvent, library);


            }
            else if(loginController.verifyLogin() && borrowBtn.getText().equals("Reserve")) {
                //apre dialog per avvenuta reservation e notifica alla biblioteca
                dialogController.createReserveDialog(actionEvent);
            }
        });
    }


    public void setInfo(LibraryBean library)
    {
        name.setText(library.getName());
        if (library.isAvailable()) {
            borrowBtn.setText("Borrow");
        }
        else {
            borrowBtn.setText("Reserve");
        }

        setBtnHandler(library);
    }

    public AnchorPane getCell()
    {
        return cell;
    }

}
