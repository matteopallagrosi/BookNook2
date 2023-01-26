package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.control.AddBookController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddBookUIController extends UIController implements Initializable {

    @FXML
    private Button addBookBtn;

    @FXML
    private TextField authorField;

    @FXML
    private CheckBox consultationBox;

    @FXML
    private BorderPane homePage;

    @FXML
    private TextField isbnField;

    @FXML
    private ComboBox<Integer> numCopiesField;

    @FXML
    private TextField publisherField;

    @FXML
    private Button reviewsBtn;

    @FXML
    private TextField titleField;

    @FXML
    private TextField yearField;

    @FXML
    private Rectangle errorPanel;

    @FXML
    private Label missingLabel;


    @FXML
    private ComboBox<?> tagsBox;

    @FXML
    private AnchorPane centralPane;

    private CheckComboBox<String> tagsComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //inizializza la combo box delle copie
        for (int i = 1; i<=30; i++) {
            numCopiesField.getItems().add(i);
        }

        //inizializza la combo box dei tag selezionabili
        AddBookController controller = new AddBookController();
        BookBean bookTags = controller.calculateAvailableTags();
        ObservableList<String> tags = FXCollections.observableArrayList(bookTags.getTags());
        tagsComboBox = new CheckComboBox<>(tags);

        centralPane.getChildren().add(tagsComboBox);
        tagsComboBox.setLayoutX(112);
        tagsComboBox.setLayoutY(442);
        tagsComboBox.setMaxWidth(150);
        tagsComboBox.setMaxHeight(25);
    }

    @FXML
    void onReviewsClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/library-review-view.fxml", event);
    }


    @FXML
    void onConfirmClick(ActionEvent event) {
        //verifica che i campi siano stati tutti compilati
        if (titleField.getText().isEmpty() || authorField.getText().isEmpty() || isbnField.getText().isEmpty() ||
            publisherField.getText().isEmpty() || yearField.getText().isEmpty() || tagsComboBox.getCheckModel().getCheckedItems().isEmpty()) {
            showMissingError();
            return;
        }


        BookBean insertedBook = new BookBean();
        insertedBook.setIsbn(isbnField.getText());
        insertedBook.setTitle(titleField.getText());
        insertedBook.setAuthor(authorField.getText());
        insertedBook.setPublisher(publisherField.getText());
        insertedBook.setYear(yearField.getText());
        insertedBook.setNumCopies(numCopiesField.getValue());
        if (!consultationBox.isSelected()) {
            insertedBook.setTags(tagsComboBox.getCheckModel().getCheckedItems());
        }
        else {
            insertedBook.setTags(new ArrayList<>());
        }

        //invoca il controller applicativo per salvataggio libro
        AddBookController controller = new AddBookController();
        controller.addABook(insertedBook);

        //apre dialog di inserimento corretto
        DialogController dialogController = new DialogController();
        dialogController.createSuccessInsertDialog(event);
    }

    private void showMissingError() {
        errorPanel.setVisible(true);
        missingLabel.setVisible(true);
    }
}
