package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.FavoriteBean;
import it.ispw.booknook.logic.control.ReadingListController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MyListsUIController extends UIController implements Initializable {

    @FXML
    private ListView<BookBean> currentList;

    @FXML
    private Rectangle booksOnLoan;

    @FXML
    private Rectangle wantToRead;

    @FXML
    private Rectangle booksILiked;

    private final static String GREY = "#8a8a8a66";
    private final static String YELLOW = "#e8be8e";

    ObservableList<BookBean> booksOnLoanList = null;
    ObservableList<BookBean> booksILikedList = null;
    ObservableList<BookBean> wantToReadList = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAvatar();
        currentList.setStyle("-fx-focus-color: transparent;");
        currentList.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/ispw/booknook/mainView/bookList.css")).toExternalForm());
        currentList.setFocusTraversable(false);
        //inizializza le tre liste con i rispettivi libri (invocando il controller applicativo)
        ReadingListController readingListController = new ReadingListController();
        //recupera lista libri in prestito
        List<BookBean> loanBooks = readingListController.getBooksOnLoan();
        booksOnLoanList = FXCollections.observableArrayList(loanBooks);
        //recupera libri lista "Want to read"
        FavoriteBean wantList = new FavoriteBean("Want to read");
        List<BookBean> wantToReadBooks = readingListController.getReadingList(wantList);
        wantToReadList = FXCollections.observableArrayList(wantToReadBooks);
        //recupera i libri della lista "Books I liked"
        FavoriteBean likeList = new FavoriteBean("Books I liked");
        List<BookBean> likedBooks = readingListController.getReadingList(likeList);
        booksILikedList = FXCollections.observableArrayList(likedBooks);
        //inizialmente compare la lista booksOnLoan
        currentList.setItems(booksOnLoanList);
        currentList.setCellFactory(listView -> new FavoritesViewCell());
    }

    @FXML
    void onBackClick(MouseEvent event) {
    }

    @FXML
    void onLikesClick(MouseEvent event) {
        booksILiked.setFill(Color.web(YELLOW));
        booksOnLoan.setFill(Color.web(GREY));
        wantToRead.setFill(Color.web(GREY));
        showList(booksILikedList);

    }

    @FXML
    void onLoanClick(MouseEvent event) {
        booksILiked.setFill(Color.web(GREY));
        booksOnLoan.setFill(Color.web(YELLOW));
        wantToRead.setFill(Color.web(GREY));
        showList(booksOnLoanList);
    }

    @FXML
    void onWantToReadClick(MouseEvent event) {
        booksILiked.setFill(Color.web(GREY));
        booksOnLoan.setFill(Color.web(GREY));
        wantToRead.setFill(Color.web(YELLOW));
        showList(wantToReadList);
    }

    @FXML
    void onProfileClick(MouseEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/settings-view.fxml", event);

    }

    @FXML
    void onDiscoverClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
    }

    @FXML
    void onConsultationClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/consultation-view.fxml", event);
    }

    //mostra la lista selezionata
    public void showList(ObservableList<BookBean> listToShow) {
        currentList.setItems(listToShow);
        currentList.setCellFactory(listView -> new FavoritesViewCell());
    }
}
