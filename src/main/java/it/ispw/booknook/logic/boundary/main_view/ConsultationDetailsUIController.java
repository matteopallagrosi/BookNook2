package it.ispw.booknook.logic.boundary.main_view;

import com.esri.arcgisruntime.mapping.view.MapView;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.ShiftBean;
import it.ispw.booknook.logic.boundary.MapViewer;
import it.ispw.booknook.logic.control.ConsultationController;
import it.ispw.booknook.logic.control.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class ConsultationDetailsUIController extends UIController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private Label addrLabel;

    @FXML
    private Label hoursLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label reviewsLabel;

    private LibraryBean currentLibrary;
    private ShiftBean currentShift;

    @FXML
    private ListView<String> timeTable;

    @FXML
    private Button confirmBtn;




    private static final String PAGE_NAME = "/it/ispw/booknook/mainView/consultation-view.fxml";

    public void initializeDetails(LibraryBean library) {

        setAvatar();
        currentLibrary = library;
        //creazione mappa con posizione biblioteca
        MapViewer mapViewer = new MapViewer();
        MapView mapView = mapViewer.createMap(library.getLatitude(), library.getLongitude());
        mapView.setId("map");
        mapPane.getChildren().add(mapView);
        mapPane.lookup("#map").prefWidth(561);
        mapPane.lookup("#map").prefHeight(244);
        AnchorPane.setTopAnchor(mapView, 139.0);
        AnchorPane.setLeftAnchor(mapView, 23.0);
        AnchorPane.setRightAnchor(mapView, 14.0);
        AnchorPane.setBottomAnchor(mapView, 26.0);
        nameLabel.setText(library.getName());
        addrLabel.setText(library.getAddress() + ", " + library.getCity());
        hoursLabel.setText(library.getOpeningTime() + " - " + library.getClosingTime());


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

    //selezione della data
    @FXML
    void onDateClick(ActionEvent event) {
        LocalDate selectedDate= datePicker.getValue();
        //chiede al controller applicativo turni disponibili
        ConsultationController controller = new ConsultationController();
        ShiftBean requestedDate = new ShiftBean();
        requestedDate.setDate(selectedDate);
        requestedDate.setUsernameLibrary(currentLibrary.getUsername());
        List<ShiftBean> shifts = controller.getAvailableShifts(requestedDate);

        //mostra soltanto gli orari disponibili
        timeTable.getItems().clear();
        shifts.forEach(shiftBean -> timeTable.getItems().add(shiftBean.getTime()));
        timeTable.setOnMouseClicked(event1 -> {
            int currentIndex = timeTable.getSelectionModel().getSelectedIndex();
            currentShift = shifts.get(currentIndex);
            confirmBtn.setDisable(false);
        });
    }

    @FXML
    void onConfirmClick(ActionEvent event) {
        //verifica login
        LoginController loginController = new LoginController();
        if (!loginController.verifyLogin()) {
            //apre un dialog per login/signup
            DialogController dialogController = new DialogController();
            dialogController.createLoginDialog();
        }
        if (loginController.verifyLogin()) {
            //apre un dialog per la conferma della prenotazione
            DialogController dialogController = new DialogController();
            dialogController.createConsultationDialog(currentShift, currentLibrary, event);
        }
    }

    @FXML
    void onBackClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PAGE_NAME));
        changePage(PAGE_NAME, event);
    }

    @FXML
    void onReviewsClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/rate-view.fxml"));
            Parent root = loader.load();
            ReviewUIController controller = loader.getController();
            controller.setReviews(currentLibrary);
            Scene scene = ((Node)(event.getSource())).getScene();
            scene.setRoot(root);
            root.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
