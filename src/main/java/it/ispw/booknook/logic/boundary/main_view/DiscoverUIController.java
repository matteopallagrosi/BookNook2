package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.control.BorrowBookController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DiscoverUIController extends UIController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultList;

    @FXML
    private Label categoriesLabel;

    @FXML
    private Button romanceBtn;

    @FXML
    private Button scifiBtn;

    @FXML
    private Button adventureBtn;

    @FXML
    private Button thrillerBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resultList.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/bookorder-view.fxml"));
                Parent root = loader.load();
                ResultsUIController controller = loader.getController();
                controller.setListView(resultList.getSelectionModel().getSelectedItem());
                Scene scene = ((Node)(mouseEvent.getSource())).getScene();
                scene.setRoot(root);
                root.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        setAvatar();
    }


    @FXML
    void onConsultationClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/consultation-view.fxml", event);
    }

    @FXML
    void onProfileClick(MouseEvent event) throws IOException {
        super.onProfileClick(event);
    }


    @FXML
    void onMyListClick(ActionEvent event) throws IOException {
        super.onMyListsClick(event);

    }

    @FXML
    void onClubsClick(ActionEvent event) throws IOException {
        changePage("/it/ispw/booknook/mainView/clubs-view.fxml", event);
    }

    @FXML
    void borrowBookByName(ActionEvent event) {
            String searchedTx = searchField.getText(); //titolo o autore libro richiesto
            BookBean searchedBook = new BookBean(searchedTx);
            BorrowBookController controller = new BorrowBookController();

            List<BookBean> results = controller.borrowByName(searchedBook);
            resultList.getItems().clear();
            results.forEach(bookBean -> resultList.getItems().add(bookBean.getTitle() + ", " + bookBean.getAuthor()));
            resultList.setVisible(true);
    }

}


