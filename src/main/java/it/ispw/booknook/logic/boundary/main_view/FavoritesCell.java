package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.Observer;
import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FavoritesCell extends Observer {

    @FXML
    private AnchorPane cell;

    @FXML
    private Label title;

    @FXML
    private Label author;

    @FXML
    private Label expireDate;

    @FXML
    private ImageView cover;

    @FXML
    private Label toBeReturned;

    @FXML
    private Label libraryName;

    @FXML
    private Button removeBtn;

    @FXML
    private Label fromLabel;

    @FXML
    private Button borrowBtn;

    @FXML
    private Label reviewLabel;

    private BookBean currentBook;




    public FavoritesCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/favoritesCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
    }

    public void setInfo(BookBean book)
    {
        currentBook = book;
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        cover.setImage(book.getCoverImage());
        if (book.getExpireDate() != null) {
            expireDate.setText(book.getExpireDate());
            toBeReturned.setVisible(true);
            libraryName.setText(book.getLibraryName());
            removeBtn.setDisable(true);
            removeBtn.setVisible(false);
            borrowBtn.setDisable(true);
            borrowBtn.setVisible(false);
            fromLabel.setVisible(true);
            reviewLabel.setVisible(true);
        }

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
                controller.displayLibraryList(book.getIsbn(), book.getTitle());
                Scene scene = ((Node) (actionEvent.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();
            }
        });

        reviewLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/newReview-view.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CreateReviewUIController controller = loader.getController();
                LibraryBean library = new LibraryBean();
                library.setUsername(book.getUsernameLibrary());
                controller.setLibraryDetails(library);
                Scene scene = ((Node) (mouseEvent.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();

            }
        });

    }

    public AnchorPane getCell()
    {
        return cell;
    }

    //quando è stata scaricata l'immagine di copertina la cella viene aggiornata
    @Override
    public void update() {
        cover.setImage(currentBook.getCoverImage());
    }
}
