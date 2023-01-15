package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.FavoriteBean;
import it.ispw.booknook.logic.control.BorrowBookController;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.control.ReadingListController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ResultsUIController extends UIController {

    @FXML
    private ListView<BookBean> bookList;

    @FXML
    private Label author;

    @FXML
    private ImageView coverImage;

    @FXML
    private Label title;

    @FXML
    private FlowPane box;

    @FXML
    private MenuButton listBtn;

    @FXML
    private Button borrowBtn;

    public void setListView(String details)
    {
        System.out.println("Sono di nuovo qui");
        box.getChildren().clear();
        setAvatar();
        BookBean bookBean = new BookBean();
        //invoca il controller applicativo
        bookBean.splitDetails(details);
        List<BookBean> results = new BorrowBookController().processRequestedBook(bookBean);
        results.get(0).setViewToUpdate(coverImage);
        author.setText(results.get(0).getAuthor());
        title.setText(results.get(0).getTitle());
        coverImage.setImage(results.get(0).getCoverImage());

        results.get(0).getTags().forEach(tag -> {
            Button tagBtn = new Button(tag);
            tagBtn.setStyle("-fx-background-radius: 8; -fx-background-color:  #c9c9c9; -fx-effect:  dropshadow(one-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 2)");
            box.getChildren().add(tagBtn);
        });

        System.out.println("Ho recuperato i tag");

        String isbn = results.get(0).getIsbn();

        //L'utente ha selezionato il libro principale
        borrowBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/borrowdetails-view.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BorrowDetailsUIController controller = loader.getController();
                controller.displayLibraryList(isbn, title.getText());
                Scene scene = ((Node)(actionEvent.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();
            }
        });

        //indica come "added i libri aggiunti ad una qualche lista"
        if (results.get(0).isAddedtoList()) {
            listBtn.setText("   Added   ");
            listBtn.setStyle("-fx-font-weight: bold; -fx-background-radius: 8; -fx-text-fill: black; -fx-background-color: white");
            listBtn.setDisable(true);
        }

        results.remove(0);
        ObservableList<BookBean> observableList = FXCollections.observableArrayList(results);
        bookList.setItems(observableList);
        bookList.setStyle("-fx-focus-color: transparent;");
        bookList.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/ispw/booknook/mainView/bookList.css")).toExternalForm());
        bookList.setFocusTraversable(false);
        bookList.setCellFactory(listView -> new ListViewCell());

        MenuItem list1 = new MenuItem("Books I liked");
        MenuItem list2 = new MenuItem("Want to read");
        listBtn.getItems().add(list1);
        listBtn.getItems().add(list2);

        EventHandler<ActionEvent> action = onFavoriteClick(isbn);

        list1.setOnAction(action);
        list2.setOnAction(action);

        System.out.println("Ho finito");
    }

    private EventHandler<ActionEvent> onFavoriteClick(String isbn) {
        return actionEvent -> {
            //prima verifica che l'utente sia loggato
            LoginController loginController = new LoginController();
            if (!loginController.verifyLogin()) {
                //apre un dialog per login/signup
                DialogController dialogController = new DialogController();
                dialogController.createLoginDialog();
            }
            //se l'utente è loggato il libro è aggiunto alla lista scelta;
            if (loginController.verifyLogin()) {
                MenuItem mItem = (MenuItem) actionEvent.getSource();
                String selectedList = mItem.getText();
                //aggiunge il libro alla lista selezionata
                ReadingListController controller = new ReadingListController();
                FavoriteBean bookToAdd = new FavoriteBean(isbn, selectedList);
                controller.addToReadingList(bookToAdd);
                listBtn.setText("   Added   ");
                listBtn.setStyle("-fx-font-weight: bold; -fx-background-radius: 8; -fx-text-fill: black; -fx-background-color: white");
                listBtn.setDisable(true);
            }
        };

    }

    @FXML
    void onDiscoverClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/homepage-view.fxml", event);
    }

    @FXML
    void onConsultationClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/consultation-view.fxml", event);
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
