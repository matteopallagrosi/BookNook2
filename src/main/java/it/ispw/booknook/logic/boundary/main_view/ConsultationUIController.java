package it.ispw.booknook.logic.boundary.main_view;


import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.control.ConsultationController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ConsultationUIController extends UIController implements Initializable {

    @FXML
    private AnchorPane listPane;

    @FXML
    private ListView<String> resultList;

    @FXML
    private TextField searchBar;

    @FXML
    private Label authorLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private ListView<String> librariesList;

    private List<BookBean> results;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAvatar();
        resultList.setOnMouseClicked(mouseEvent -> {
            int selectedBookIndex = resultList.getSelectionModel().getSelectedIndex();
            BookBean selectedBook = results.get(selectedBookIndex);
            titleLabel.setText(selectedBook.getTitle());
            authorLabel.setText(selectedBook.getAuthor());
            yearLabel.setText(selectedBook.getPublishingYear());
            //calcolare biblioteche con disponibilità
            createListLibraries(selectedBook);
        });
    }

    public void createListLibraries(BookBean book) {
        //compare la lista delle biblioteche con disponiilità
        ConsultationController consultationController = new ConsultationController();
        List<LibraryBean> libraries = consultationController.checkLibraries(book);
        librariesList.getItems().clear();
        libraries.forEach(library -> librariesList.getItems().add(library.getName()));
        listPane.setVisible(true);

        //cambia schermata quando clicco sulla libreria -->
        librariesList.setOnMouseClicked(event -> {

            int libraryIndex = librariesList.getSelectionModel().getSelectedIndex();
            LibraryBean selectedLibrary = libraries.get(libraryIndex);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/consultationdetails-view.fxml"));
                Parent root = loader.load();
                ConsultationDetailsUIController controller = loader.getController();
                controller.initializeDetails(selectedLibrary);
                Scene scene = ((Node)(event.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void reserveConsultation(ActionEvent event) {
        String searchedTx = searchBar.getText(); //titolo o autore libro richiesto
        //invoca controller applicativo per recuperare libri
        BookBean searchedBook = new BookBean(searchedTx);
        ConsultationController consultationController = new ConsultationController();
        results = consultationController.searchBook(searchedBook);
        resultList.getItems().clear();
        results.forEach(bookBean -> resultList.getItems().add(bookBean.getTitle() + ", " + bookBean.getAuthor()));
        resultList.setVisible(true);
    }
}
