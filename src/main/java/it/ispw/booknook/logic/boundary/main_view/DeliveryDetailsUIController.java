package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.CreditCardBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.boundary.BookDetailsBoundary;
import it.ispw.booknook.logic.control.BorrowBookController;
import it.ispw.booknook.logic.entity.User;
import it.ispw.booknook.logic.exception.FormatException;
import it.ispw.booknook.logic.exception.InvalidDateException;
import it.ispw.booknook.logic.exception.LostConnectionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
        Thread t1 = new Thread(new BookDetailsBoundary(book));
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
        card.setCode(cardCode);
        card.setOwnerName(cardName);
        try {
            card.setNumber(cardNumber);
            card.setExpiryDate(cardDate);
        } catch (FormatException | InvalidDateException e) {
            invalidCardLabel.setText(e.getMessage());
            invalidCardLabel.setVisible(true);
            return;
        }
        //procede con l'ordine
        BorrowBookController borrowBookController = new BorrowBookController();
        try {
            borrowBookController.borrowBook(currentDetails);
        } catch (LostConnectionException e) {
            //Prestito con successo ma mancato invio email per errore connessione
            DialogController dialogController = new DialogController();
            dialogController.successLoanDialog(event, "Loan has been successfull! " + e.getMessage());
            return;
        }
        //apre dialog successo e torna a homepage
        DialogController dialogController = new DialogController();
        dialogController.successLoanDialog(event, "Loan has been successfull! You will receive an email with the order details.");
    }
}
