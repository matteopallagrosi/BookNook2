package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.CreditCardBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.control.BorrowBookController;
import it.ispw.booknook.logic.exception.BookNotFoundException;
import it.ispw.booknook.logic.exception.FormatException;
import it.ispw.booknook.logic.exception.InvalidDateException;
import it.ispw.booknook.logic.exception.LostConnectionException;

import java.util.ArrayList;
import java.util.List;

public class BorrowBookCLController {

    private static final String NEW_LINE = " ***\n";
    private static final String INVALID = "Invalid condition";

    public void borrowBookByName() {
        InOut.printLine("*** Insert title or author ***\n");

        String search = "";

        while (search.isEmpty()) {
            InOut.print("Title or author: ");
            search = InOut.readLine();
        }

        BookBean requestedBook = new BookBean(search);
        BorrowBookController borrowBookController = new BorrowBookController();
        try {
            List<BookBean> results = borrowBookController.borrowByName(requestedBook);

            InOut.printLine("\n");

            for (int i = 1; i <= results.size(); i++) {
                InOut.printLine(i + ") " + results.get(i - 1).getTitle() + ", " + results.get(i - 1).getAuthor());
            }

            int choice = InOut.multiIntChoice(results.size());

            BookBean selectedBook = results.get(choice - 1);

            //invoca controller applicativo per calcolare lista libri simili (+ libro scelto)
            List<BookBean> related = new BorrowBookController().processRequestedBook(selectedBook);

            InOut.printLine("\n");

            InOut.printLine("*** Here your book and some similar books ***\n");

            //visualizza la lista dei libri simili
            showBookList(related);
        } catch (BookNotFoundException e) {
            InOut.printLine(e.getMessage());
        }
    }

    public void borrowBookByTag() {
        InOut.printLine("*** Choose a category ***\n");

        InOut.printLine("1) Sci-fi");

        InOut.printLine("2) Romance");

        InOut.printLine("3) Thriller");

        InOut.printLine("4) Adventure");

        InOut.printLine("5) Horror");

        InOut.printLine("6) Poetry");

        InOut.printLine("7) Fantasy");

        InOut.printLine("8) Dystopia");

        char[] options = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};

        char op = InOut.multiChoice(options, 8);
        String selectedTag = switch (op) {
            case '1' -> "Sci-fi";
            case '2' -> "Romance";
            case '3' -> "Thriller";
            case '4' -> "Adventure";
            case '5' -> "Horror";
            case '6' -> "Poetry";
            case '7' -> "Fantasy";
            case '8' -> "Dystopia";
            default -> throw new IllegalStateException("Unexpected value: " + op);
        };
        BookBean bookBean = new BookBean();
        List<String> tags = new ArrayList<>();
        tags.add(selectedTag);
        bookBean.setTags(tags);
        List<BookBean> results = new BorrowBookController().borrowByTag(bookBean);

        InOut.printLine("*** Here are some books in the category " + selectedTag + NEW_LINE);
        showBookList(results);
    }

    //mostra librerie con disponibilità
    public void borrowABook(BookBean book) {
        InOut.printLine("*** Choose a library ***\n");
        BorrowBookController borrowBookController = new BorrowBookController();
        List<LibraryBean> libraries = borrowBookController.calculateLibraries(book);
        for (int i = 1; i <= libraries.size(); i++) {
            String status = "";
            if (libraries.get(i - 1).isAvailable()) {
                status = "borrow";
            } else {
                status = "reserve";
            }
            InOut.printLine(i + ") " + libraries.get(i - 1).getName() + " [ " +
                    libraries.get(i - 1).getAddress() + ", " + libraries.get(i - 1).getCity() + ", " +
                    libraries.get(i - 1).getOpeningTime() + " - " + libraries.get(i - 1).getClosingTime() + " ]   (" + status + ")");

        }
        int libraryChoice = InOut.multiIntChoice(libraries.size());
        LibraryBean selectedLibrary = libraries.get(libraryChoice - 1);

        InOut.printLine("*** Proceed with loan or read/write reviews of " + selectedLibrary.getName() + NEW_LINE);


        InOut.printLine("1) Proceed with loan");
        InOut.printLine("2) Read reviews");
        InOut.printLine("3) Write a review");

        char[] options = new char[]{'1', '2', '3'};

        char op = InOut.multiChoice(options, 3);
        ReviewCLController reviewCLController = new ReviewCLController();

        switch (op) {
            case '1' -> proceedWithLoan(selectedLibrary, borrowBookController);
            case '2' -> reviewCLController.showReviews(selectedLibrary);
            case '3' -> reviewCLController.writeAReview(selectedLibrary);
            default -> {
                InOut.printLine(INVALID);
            }
        }
    }

    private void proceedWithLoan(LibraryBean selectedLibrary, BorrowBookController borrowBookController) {
        if (!selectedLibrary.isAvailable()) {
            InOut.printLine("*** Book correctly reserved ***\n");
            return;
        }

        InOut.printLine("*** Choose a delivery method ***\n");

        InOut.printLine("1) In-library pickup");

        InOut.printLine("2) Home delivery");


        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);

        switch (op) {
            case '1' -> {
                try {
                    borrowBookController.borrowBook(selectedLibrary);
                } catch (LostConnectionException e) {
                    InOut.printLine("*** You can pickup your book within three days! "  + e.getMessage() + NEW_LINE);
                    return;
                }
                InOut.printLine("*** You can pickup your book within three days, you will receive a summary email ***\n");
            }
            case '2' -> choosePaymentMethod(selectedLibrary);
            default -> InOut.printLine(INVALID);
        }
    }


    private void showBookList(List<BookBean> results) {
        for (int i = 1; i <= results.size(); i++) {
            InOut.print(i + ") " + results.get(i - 1).getTitle() + ", " + results.get(i - 1).getAuthor() + " [");
            int numTags = results.get(i - 1).getTags().size();
            for (int j = 0; j < numTags - 1; j++) {
                InOut.print(results.get(i - 1).getTags().get(j) + ", ");
            }
            InOut.print(results.get(i - 1).getTags().get(numTags - 1));
            InOut.print("]\n");
        }

        int bookChoice = InOut.multiIntChoice(results.size());

        //libro scelto dall'utente
        BookBean bookToBorrow = results.get(bookChoice - 1);

        InOut.printLine("*** You choose " + bookToBorrow.getTitle() + NEW_LINE);

        InOut.printLine("1) Borrow");

        InOut.printLine("2) Add to a list");

        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);

        switch (op) {
            case '1' -> borrowABook(bookToBorrow);
            case '2' -> {
                if (bookToBorrow.isAddedtoList()) InOut.printLine("Book already added");
                else {
                    ReadingListCLController readingListCLController = new ReadingListCLController();
                    readingListCLController.addBookToAList(bookToBorrow);
                }
            }
            default -> InOut.printLine(INVALID);

        }
    }

    //scelta del metodo di pagamento e conclusione ordine
    private void choosePaymentMethod(LibraryBean library){
        InOut.printLine("*** Enter shipping information ***\n");
        String street = "";
        String country = "";
        String city = "";
        while (street.isEmpty()) {
            InOut.print("Street: ");
            street = InOut.readLine();
        }
        while (city.isEmpty()) {
            InOut.print("City: ");
            city = InOut.readLine();

        }
        while (country.isEmpty()) {
            InOut.print("Country: ");
            country = InOut.readLine();
        }
        int status = 1;

        while(status==1) {
            InOut.printLine("*** Insert your payment credentials ***\n");
            String nameOnCard = "";
            String cardNumber = "";
            String expiryDate = "";
            String securityCode = "";

            while (nameOnCard.isEmpty()) {
                InOut.print("Name On Card: ");
                nameOnCard = InOut.readLine();
            }
            while (cardNumber.isEmpty()) {
                InOut.print("Card Number: ");
                cardNumber = InOut.readLine();

            }
            while (expiryDate.isEmpty()) {
                InOut.print("Expiry Date[mm/dd]: ");
                expiryDate = InOut.readLine();

            }
            while (securityCode.isEmpty()) {
                InOut.print("Security Code: ");
                securityCode = InOut.readLine();
            }

            try {
                new CreditCardBean(cardNumber, securityCode, nameOnCard, expiryDate);
            } catch (FormatException | InvalidDateException e) {
                InOut.printLine(e.getMessage());
                continue;
            }
            status = 0;
        }
        //procede con l'ordine
        BorrowBookController borrowBookController = new BorrowBookController();
        try {
            borrowBookController.borrowBook(library);
        } catch(LostConnectionException e) {
            InOut.printLine("Loan has been successful! " + e.getMessage());
            return;
        }
        InOut.printLine("Loan has been successful! You will receive an email with the order details.");

    }
}
