package it.ispw.booknook.logic.boundary.main_view;

import com.esri.arcgisruntime.mapping.view.MapView;
import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.boundary.MapViewer;
import it.ispw.booknook.logic.control.BorrowBookController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class BorrowDetailsUIController extends UIController {

    @FXML
    private ListView<LibraryBean> libraryList;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label hoursLabel;

    @FXML
    private Label addrLabel;

    @FXML
    private MenuButton cityFilter;


    private MapView mapView;


    @FXML
    private Label introLabel;

    @FXML
    private Label reviewsLabel;

    private LibraryBean currentLibrary;


    public void displayLibraryList(String isbn, String title) {
        setAvatar();
        introLabel.setText("'" + title + "' is available in the following libraries.");
        MapViewer mapViewer = new MapViewer();


        BorrowBookController controller = new BorrowBookController();
        BookBean book = new BookBean();
        book.setIsbn(isbn);
        //recupera la lista di librerie con disponibilità (borrow/reserve) della copia
        List<LibraryBean> libraries = controller.calculateLibraries(book);

        currentLibrary = libraries.get(0);

        ObservableList<LibraryBean> observableList = FXCollections.observableArrayList(libraries);
        libraryList.setItems(observableList);
        libraryList.setStyle("-fx-focus-color: transparent;");
        libraryList.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/ispw/booknook/mainView/libraryList.css")).toExternalForm());
        libraryList.setFocusTraversable(false);
        libraryList.setCellFactory(listView -> new LibraryViewCell());
        libraryList.getSelectionModel().selectedItemProperty().addListener((observableValue, libraryBean, t1) -> {
            nameLabel.setText(t1.getName());
            addrLabel.setText(t1.getAddress() + ", " + t1.getCity());
            hoursLabel.setText(t1.getOpeningTime() + " - " + t1.getClosingTime());
            mapViewer.changePosition(t1.getLatitude(), t1.getLongitude());
            currentLibrary = t1;

        });

        nameLabel.setText(libraries.get(0).getName());
        addrLabel.setText(libraries.get(0).getAddress() + ", " + libraries.get(0).getCity());
        hoursLabel.setText(libraries.get(0).getOpeningTime() + " - " + libraries.get(0).getClosingTime());

        // create a MapView to display the map and add it to the AnchorPane
        mapView = mapViewer.createMap(libraries.get(0).getLatitude(), libraries.get(0).getLongitude());
        mapView.setId("map");
        mapPane.getChildren().add(mapView);
        mapPane.lookup("#map").prefWidth(330);
        mapPane.lookup("#map").prefHeight(220);
        AnchorPane.setTopAnchor(mapView, 196.0);
        AnchorPane.setLeftAnchor(mapView, 61.0);
        AnchorPane.setRightAnchor(mapView, 91.0);
        AnchorPane.setBottomAnchor(mapView, 15.0);

        MenuItem defaultItem = new MenuItem("All");
        defaultItem.setOnAction(cityFilterHandler(defaultItem.getText(), libraries));
        cityFilter.getItems().add(defaultItem);
        HashMap<String, MenuItem> items = new HashMap<>();
        for (int i = 0; i < libraries.size(); i++) {
            String city = libraries.get(i).getCity();
            if (items.get(city) == null) {
                MenuItem item = new MenuItem(city);
                item.setOnAction(cityFilterHandler(item.getText(), libraries));
                items.put(city, item);
                cityFilter.getItems().add(item);
            }
        }

    }

    private EventHandler<ActionEvent> cityFilterHandler(String selectedCity, List<LibraryBean> libraries) {
        return actionEvent -> {
            cityFilter.setText(selectedCity);
            libraryList.getItems().clear();
            //se l'utente preme "All" ricompare la lista completa
            if (selectedCity.equals("All")) {
                ObservableList<LibraryBean> completeList = FXCollections.observableArrayList(libraries);
                libraryList.setItems(completeList);
            }
            //altrimenti mostra solo librerie della città selezionata
            else {
                List<LibraryBean> newList = new ArrayList<>();
                for (int i = 0; i < libraries.size(); i++) {
                    String city = libraries.get(i).getCity();
                    if (city.equals(selectedCity)) {
                        newList.add(libraries.get(i));
                    }
                }
                ObservableList<LibraryBean> filteredList = FXCollections.observableArrayList(newList);
                libraryList.setItems(filteredList);
            }
        };
    }

    @FXML
    void onReviewsClick(MouseEvent event) {
        //apre pagina recensioni libreria
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

    @FXML
    public void onBackClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
    }
}
