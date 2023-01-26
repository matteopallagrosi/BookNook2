package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.Observer;
import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.FavoriteBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.control.ReadingListController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class BookCell extends Observer {

    @FXML
    private AnchorPane cell;

    @FXML
    private MenuButton addToListBtn;

    @FXML
    private Label author;

    @FXML
    private Button borrowBtn;

    @FXML
    private ImageView cover;

    @FXML
    private Button tag1;

    @FXML
    private Button tag2;

    @FXML
    private Label title;

    @FXML
    private FlowPane box;

    private BookBean currentBook;



    public BookCell()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/listCellItem.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private void setFavorites(BookBean book) {
        //recuperare liste effettive
        MenuItem list1 = new MenuItem("Books I liked");
        MenuItem list2 = new MenuItem("Want to read");
        addToListBtn.getItems().add(list1);
        addToListBtn.getItems().add(list2);

        EventHandler<ActionEvent> action = onFavoriteClick(book);

        list1.setOnAction(action);
        list2.setOnAction(action);


    }

    private EventHandler<ActionEvent> onFavoriteClick(BookBean book) {
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
                System.out.println("Sono rientrato");
                MenuItem mItem = (MenuItem) actionEvent.getSource();
                String selectedList = mItem.getText();
                //aggiunge il libro alla lista selezionata
                ReadingListController controller = new ReadingListController();
                FavoriteBean bookToAdd = new FavoriteBean(book.getIsbn(), selectedList);
                controller.addToReadingList(bookToAdd);
                book.setAddedtoList(true);
                addToListBtn.setText("   Added   ");
                addToListBtn.setStyle("-fx-font-weight: bold; -fx-background-radius: 8; -fx-text-fill: black; -fx-background-color: white");
                addToListBtn.setDisable(true);
            }
        };

    }

    public void setInfo(BookBean book)
    {
        currentBook = book;
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        cover.setImage(book.getCoverImage());
        book.getTags().forEach(tag -> {
            Button tagBtn = new Button(tag);
            tagBtn.setStyle("-fx-background-radius: 8; -fx-background-color:  #c9c9c9; -fx-effect:  dropshadow(one-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 2)");
            box.getChildren().add(tagBtn);
            tagBtn.setOnAction(actionEvent -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/tagResults-view.fxml"));
                    Parent root = loader.load();
                    TagResultsUIController controller = loader.getController();
                    controller.setListView(tag);
                    Scene scene = ((Node)(actionEvent.getSource())).getScene();
                    scene.setRoot(root);
                    root.requestFocus();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        borrowBtn.setOnAction(new EventHandler<>() {
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
                controller.displayLibraryList(book.getIsbn(), title.getText());
                Scene scene = ((Node) (actionEvent.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();
            }
        });

        //inizializzare le liste dei preferiti
        setFavorites(book);

        if (book.isAddedtoList()) {
            addToListBtn.setText("   Added   ");
            addToListBtn.setStyle("-fx-font-weight: bold; -fx-background-radius: 8; -fx-text-fill: black; -fx-background-color: white");
            addToListBtn.setDisable(true);
        }
    }

    public AnchorPane getCell()
    {
        return cell;
    }

    @Override
    public void update() {
        cover.setImage(currentBook.getCoverImage());
    }
}
