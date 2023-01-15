package it.ispw.booknook.logic.boundary.main_view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConsultationDetailsUIController extends UIController implements Initializable {

    @FXML
    private DatePicker datePicker;



    private static final String PAGE_NAME = "/it/ispw/booknook/mainView/consultation-view.fxml";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAvatar();

        datePicker.setStyle("-fx-focus-color: transparent;");
        datePicker.setDayCellFactory(p -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
    }


    @FXML
    void onDiscoverClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
    }

    @FXML
    void onConsultationClick(ActionEvent event) throws IOException {
        changePage(PAGE_NAME, event);
    }

    //selezione della data
    @FXML
    void onDateClick(ActionEvent event) {
        System.out.println("OK");
        //mostra gli orari disponibili per quella data
        DatePicker picker = (DatePicker)event.getSource();
        ListView<String> timeTable = (ListView<String>) picker.getScene().lookup("#timeTable");
        //mostra soltanto gli orari disponibili
        ObservableList<String> times = FXCollections.observableArrayList("8.00", "9.30", "10.15", "10.30",
                "11.00", "11.30", "15.15", "16.30");
        timeTable.setItems(times);
        timeTable.setOnMouseClicked(event1 -> {
            Button confirm = (Button)((Node)event1.getSource()).getParent().lookup("#confirmBtn");
            confirm.setDisable(false);
        });

    }

    @FXML
    void onConfirmClick(ActionEvent event) throws IOException {
        //apre un dialog per la conferma della prenotazione
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirm consultation");
        ButtonType type = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
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
            //conferma la prenotazione, invia email, riporta alla schermata iniziale
            changePage(PAGE_NAME, event);
        }
    }

    @FXML
    void onBackClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PAGE_NAME));
        changePage(PAGE_NAME, event);
        ConsultationUIController controller = fxmlLoader.<ConsultationUIController>getController();
        ObservableList<String> items = FXCollections.observableArrayList("Primo", "Secondo", "Terzo", "Quarto",
                "Quinto", "Sesto", "Settimo", "Ottavo");
        controller.createListLibraries(items);
    }

    @FXML
    void onProfileClick(MouseEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/settings-view.fxml", event);
    }

    @FXML
    void onMyListClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/myLists-view.fxml", event);
    }
}
