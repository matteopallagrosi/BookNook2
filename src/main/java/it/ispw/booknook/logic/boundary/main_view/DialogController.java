package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.bean.ShiftBean;
import it.ispw.booknook.logic.control.*;
import it.ispw.booknook.logic.entity.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

//opera come controller grafico per i dialog
public class DialogController extends UIController {

    private static final String LOGIN = "Login";
    private static final String SIGNUP = "Sign up";
    private static final String CONFIRM = "Confirm";
    private static final String STYLESHEET = "/it/ispw/booknook/mainView/buttonYellow.css";
    private static final String CANCEL = "Cancel";
    private static final String STYLE = "-fx-font-size: 15px;" + "-fx-font-family: Roboto ";
    private static final String HOMEPAGE = "/it/ispw/booknook/mainView/homepage-view.fxml";
    private static final String DELETE_TITLE = "Delete account";

    public void createLoginDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle(LOGIN);
        dialog.setHeaderText("Please log in");
        dialog.setResizable(true);

        Label label1 = new Label("Email: ");
        Label label2 = new Label("Password: ");
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label label3 = new Label("Don't have an account?");
        Button choiceBtn = new Button(SIGNUP);
        Label label4 = new Label("Username");
        label4.setVisible(false);
        TextField usernameField = new TextField();
        usernameField.setVisible(false);
        Label errorLabel = new Label("Invalid email or password");
        errorLabel.setVisible(false);

        GridPane grid = new GridPane();
        grid.setHgap(7);
        grid.setVgap(5);
        grid.add(label1, 1, 1);
        grid.add(emailField, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(passwordField, 2, 2);
        grid.add(label3, 1, 4);
        grid.add(choiceBtn, 2, 4);
        grid.add(label4, 1,3);
        grid.add(usernameField, 2, 3);
        grid.add(errorLabel, 3, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType(CONFIRM, ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(CANCEL, ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        choiceBtn.setOnAction(actionEvent -> {
            switch (choiceBtn.getText()) {
                case SIGNUP -> {
                    choiceBtn.setText(LOGIN);
                    label3.setText("Have an account?");
                    label4.setVisible(true);
                    usernameField.setVisible(true);
                    dialog.setTitle(SIGNUP);
                    dialog.setHeaderText("Please sign up");
                    errorLabel.setVisible(false);
                    emailField.clear();
                    passwordField.clear();
                }
                case LOGIN -> {
                    choiceBtn.setText(SIGNUP);
                    label3.setText("Don't have an account?");
                    label4.setVisible(false);
                    usernameField.setVisible(false);
                    dialog.setTitle(LOGIN);
                    dialog.setHeaderText("Please log in");
                    errorLabel.setVisible(false);
                    emailField.clear();
                    passwordField.clear();
                    usernameField.clear();
                }
                default -> errorLabel.setVisible(false);
            }
        });

        Button btnOk = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        btnOk.addEventFilter(ActionEvent.ACTION, event -> {
            LoginBean loginBean = new LoginBean();
            loginBean.setEmail(emailField.getText());
            loginBean.setPassword(passwordField.getText());
            switch (dialog.getTitle()) {
                case LOGIN -> {
                    LoginController controller = new LoginController();
                    if (!controller.checkUserLogged(loginBean)) {
                        event.consume();
                        errorLabel.setVisible(true);
                    }
                }
                case SIGNUP -> {
                    SignUpController signUpController = new SignUpController();
                    loginBean.setUsername(usernameField.getText());
                    if (loginBean.getEmail() == null || loginBean.getPassword() == null) {
                        errorLabel.setVisible(true);
                        event.consume();
                    } else signUpController.registerUser(loginBean);
                }
                default -> errorLabel.setVisible(true);
            }

        });

        dialog.showAndWait();
    }

    public void createBorrowDialog(ActionEvent actionEvent, LibraryBean library) {
        String[] choices= {"In-library pickup", "Home delivery"};
        ChoiceDialog d = new ChoiceDialog(choices[0], choices);
        d.setTitle("Delivery method");
        d.setHeaderText("Delivery method");
        d.setContentText("Choose your delivery method");
        d.getDialogPane().setStyle(STYLE);
        ((Button) d.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(CANCEL);
        d.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(STYLESHEET)).toExternalForm());
        Optional<Object> result =  d.showAndWait();
        //selezionato ritiro in libreria
        if (result.isPresent() && result.get().equals("In-library pickup")) {
            createPickUpDialog(actionEvent, library);
        }
        //selezionata consegna a casa
        else if (result.isPresent() && result.get().equals("Home delivery")) {
            //passa a schermata con informazioni di spedizione
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/deliverydetails-view.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DeliveryDetailsUIController controller = loader.getController();
            controller.setInitialDetails(library);
            Scene scene = ((Node) (actionEvent.getSource())).getScene();
            scene.setRoot(root);
            root.requestFocus();
        }
    }

    private void createPickUpDialog(ActionEvent actionEvent, LibraryBean library) {
        //result contiene la stringa In-libraryPickup o homedelivery
        //se homedelivery apre pagina metodo di consegna
        //se pickup informa sui tempi di ritiro
        Dialog<ButtonType> dialogPickup = new Dialog<>();
        dialogPickup.setTitle("In-library Pickup");
        dialogPickup.setContentText("You can pick up your book within three days.");
        ButtonType confirm = new ButtonType(CONFIRM, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(CANCEL, ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPickup.getDialogPane().getButtonTypes().add(confirm);
        dialogPickup.getDialogPane().getButtonTypes().add(cancel);
        dialogPickup.getDialogPane().setStyle(STYLE);
        dialogPickup.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(STYLESHEET)).toExternalForm());
        Optional<ButtonType> result = dialogPickup.showAndWait();
        if (result.isPresent() && result.get() == confirm) {
            //aggiorna db e manda mail di conferma (delega il controller applicativo)
            BorrowBookController borrowBookController = new BorrowBookController();
            borrowBookController.borrowBook(library);
            //ritorna alla pagine iniziale
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(HOMEPAGE)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = ((Button) (actionEvent.getSource())).getScene();
            scene.setRoot(root);
            assert root != null;
            root.requestFocus();
        }
    }

    public void createReserveDialog(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirm reservation");
        ButtonType type = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText("The librarian has received your reservation,\nyou are queuing now.\nThank you!");
        dialog.getDialogPane().setStyle(STYLE);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(STYLESHEET)).toExternalForm());

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == type) {
            //conferma la prenotazione, invia email, riporta alla schermata iniziale
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(HOMEPAGE)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = ((Button)(actionEvent.getSource())).getScene();
            scene.setRoot(root);
            assert root != null;
            root.requestFocus();
        }

    }

    public void createDeleteDialog(Event event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(DELETE_TITLE);
        alert.setHeaderText(DELETE_TITLE);
        alert.setContentText("By confirming you will lose all data associated with your account. Are you sure?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(CONFIRM);
        ButtonType cancel = new ButtonType(CANCEL, ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().add(cancel);
        alert.getDialogPane().setStyle(STYLE);
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(STYLESHEET)).toExternalForm());
        alert.getDialogPane().setPrefWidth(300);
        Optional result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SettingsController settingsController = new SettingsController();
            settingsController.deleteAccount();
            //apre dialog conferma
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle(DELETE_TITLE);
            dialog.setContentText("Account successfully deleted!");
            ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(okButtonType);
            Optional newResult = dialog.showAndWait();
            if (newResult.isPresent() && newResult.get() == okButtonType) {
                //ritorno all'homePage
                try {
                    changePage(HOMEPAGE, event);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void successLoanDialog(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Successfull loan");
        dialog.setContentText("Loan has been successfull! You will receive an email with the order details.");
        ButtonType confirm = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(confirm);
        dialog.getDialogPane().setStyle(STYLE);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(STYLESHEET)).toExternalForm());
        Optional result = dialog.showAndWait();
        if (result.isPresent() && result.get() == confirm) {
            //riporta alla schermata iniziale
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(HOMEPAGE)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = ((Button)(actionEvent.getSource())).getScene();
            scene.setRoot(root);
            assert root != null;
            root.requestFocus();
        }
    }

    public void createConsultationDialog(ShiftBean currentShift, LibraryBean currentLibrary, ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirm consultation");
        ButtonType type = new ButtonType(CONFIRM, ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText("Please confirm your reservation.\nYou will receive an email with the details.");
        dialog.getDialogPane().setStyle("-fx-font-size: 15px;" +
                "-fx-font-family: Roboto ");
        dialog.getDialogPane().getButtonTypes().add(type);
        ButtonBar buttonBar = (ButtonBar)dialog.getDialogPane().lookup(".button-bar");
        buttonBar.getButtons().forEach(b -> b.setStyle("-fx-background-color: #e9bf8e;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 2);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Roboto Medium'"));

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == type) {
            //invoca il controller applicativo per aggiornamento db e invio email

            //conferma la prenotazione, invia email, riporta alla schermata iniziale
            try {
                changePage("/it/ispw/booknook/mainView/consultation-view.fxml", event);
            } catch(IOException e) {
                 e.printStackTrace();
            }
            //invoca il controller applicativo per aggiornamento db e invio email
            ConsultationController controller = new ConsultationController();
            controller.reserveConsultation(currentShift, currentLibrary);
        }
    }

    public void createSuccessInsertDialog(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Successfull insertion");
        dialog.setContentText("Your book has been correctly added in the system!");
        ButtonType confirm = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(confirm);
        dialog.getDialogPane().setStyle(STYLE);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(STYLESHEET)).toExternalForm());
        Optional result = dialog.showAndWait();
        if (result.isPresent() && result.get() == confirm) {
            //riporta alla schermata iniziale
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/it/ispw/booknook/mainView/librarian-home-view.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = ((Button)(event.getSource())).getScene();
            scene.setRoot(root);
            assert root != null;
            root.requestFocus();
        }
    }
}
