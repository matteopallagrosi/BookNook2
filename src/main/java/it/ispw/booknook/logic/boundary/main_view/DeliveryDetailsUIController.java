package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.CreditCardBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.boundary.JSONManager;
import it.ispw.booknook.logic.control.BorrowBookController;
import it.ispw.booknook.logic.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DeliveryDetailsUIController extends UIController {

    @FXML
    private Label authorLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label libraryLabel;

    @FXML
    private TextField streetField;

    @FXML
    private TextField zipField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField countryField;

    @FXML
    private ImageView cover;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField numberField;

    @FXML
    private TextField codeField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField cardNameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label invalidCardLabel;

    @FXML
    private Label shippingErrorLabel;

    private LibraryBean currentDetails;

    @FXML
    private Label dateErrorLabel;


    public void setInitialDetails(LibraryBean library) {
        currentDetails = library;
        setAvatar();
        //recupera l'indirizzo di spedizione se presente
        String address = User.getUser().getAddress();
        if (address != null) {
            streetField.setText(address);
        }
        String city = User.getUser().getCity();
        if (address != null) {
            cityField.setText(city);
        }
        String country = User.getUser().getCountry();
        if (address != null) {
            countryField.setText(country);
        }
        String zip = User.getUser().getZip();
        if (zip != null) {
            zipField.setText(zip);
        }

        titleLabel.setText(library.getTitleCopyAvailable());
        authorLabel.setText(library.getAuthorCopyAvailable());
        libraryLabel.setText(library.getName());

        BookBean book = new BookBean();
        book.setViewToUpdate(cover);
        book.setIsbn(library.getIsbnAvailableBook());
        Thread t1 = new Thread(new JSONManager(book));
        t1.start();
    }

    @FXML
    void onConfirmClick(ActionEvent event) {
        errorLabel.setVisible(false);
        invalidCardLabel.setVisible(false);
        shippingErrorLabel.setVisible(false);
        dateErrorLabel.setVisible(false);
        String cardNumber = numberField.getText();
        String cardName = cardNameField.getText();
        String cardCode = codeField.getText();
        String cardDate = dateField.getText();
        String address = streetField.getText();
        String city = cityField.getText();
        String zip = zipField.getText();
        String country = countryField.getText();
        if (cardNumber.isEmpty() || cardName.isEmpty() || cardCode.isEmpty() || cardDate.isEmpty()) {
            errorLabel.setVisible(true);
            return;
        }
        if (address.isEmpty() || city.isEmpty() || zip.isEmpty() || country.isEmpty()) {
            shippingErrorLabel.setVisible(true);
            return;
        }

        CreditCardBean card = new CreditCardBean();
        //il bean esegue la validazione sintattica del numero di carta
        card.setNumber(cardNumber);
        card.setCode(cardCode);
        card.setOwnerName(cardName);
        card.setExpiryDate(cardDate);
        if (card.getNumber() == null) {
            invalidCardLabel.setVisible(true);
            return;
        }
        if (card.getExpiryDate() == null) {
            dateErrorLabel.setVisible(true);
            return;
        }
        //procede con l'ordine
        BorrowBookController borrowBookController = new BorrowBookController();
        borrowBookController.borrowBook(currentDetails);
        //apre dialog successo e torna a homepage
        DialogController dialogController = new DialogController();
        dialogController.successLoanDialog(event);
    }
}
