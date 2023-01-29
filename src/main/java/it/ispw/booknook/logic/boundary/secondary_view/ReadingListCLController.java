package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.FavoriteBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.control.ReadingListController;


import java.util.List;

public class ReadingListCLController {

    public void manageReadingList() {
        InOut.printLine("*** Here your reading lists. Choose a list ***\n");
        InOut.printLine("1) Books on loan");
        InOut.printLine("2) Books I liked");
        InOut.printLine("3) Want to read");

        char[] options = new char[]{'1', '2', '3'};

        char op = InOut.multiChoice(options, 3);

        FavoriteBean favoriteBean = new FavoriteBean();
        ReadingListController readingListController = new ReadingListController();
        List<BookBean> results;

        switch (op) {
            case '1' -> {
                results = readingListController.getBooksOnLoan();
                showBooksOnLoan(results);
            }
            case '2' -> {
                favoriteBean.setListName("Books I liked");
                results = readingListController.getReadingList(favoriteBean);
                showList(results, favoriteBean, readingListController);
            }
            case '3' -> {
                favoriteBean.setListName("Want to read");
                results = readingListController.getReadingList(favoriteBean);
                showList(results, favoriteBean, readingListController);
            }
            default -> InOut.printLine("Invalid condition");
        }

    }

    private void showBooksOnLoan(List<BookBean> results){
        InOut.printLine("*** Books on loan ***\n");
        for (int i = 1; i <= results.size(); i++) {
            InOut.printLine(i + ") " + results.get(i - 1).getTitle() + ", " + results.get(i - 1).getAuthor() + " [Expired date: " + results.get(i-1).getExpireDate() + ", From: " + results.get(i-1).getLibraryName() + "]");
        }

        InOut.printLine("*** Read or write reviews of a library you have been to ***\n");

        int choice = InOut.multiIntChoice(results.size());

        BookBean selectedBook = results.get(choice - 1);

        InOut.printLine("1) Read reviews of " + selectedBook.getLibraryName());
        InOut.printLine("2) Write a review");

        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);

        switch (op) {
            case '1' -> {
                getReviews(selectedBook);
            }
            case '2' -> {
                writeAReview(selectedBook);
            }
            default -> InOut.printLine("Invalid condition");
        }
    }

    private void getReviews(BookBean selectedBook) {
        LibraryBean library = new LibraryBean();
        library.setUsername(selectedBook.getUsernameLibrary());
        ReviewCLController reviewCLController = new ReviewCLController();
        reviewCLController.showReviews(library);

    }

    private void writeAReview(BookBean selectedBook) {

        LibraryBean library = new LibraryBean();
        library.setUsername(selectedBook.getUsernameLibrary());
        ReviewCLController reviewCLController = new ReviewCLController();
        reviewCLController.writeAReview(library);
    }


    private void showList(List<BookBean> results, FavoriteBean bean, ReadingListController controller){
        InOut.printLine("*** " + bean.getListName() + " ***\n");
        for (int i = 1; i <= results.size(); i++) {
            InOut.printLine(i + ") " + results.get(i - 1).getTitle() + ", " + results.get(i - 1).getAuthor());
        }
        InOut.printLine("*** Select a book to borrow or remove ***\n");

        int choice = InOut.multiIntChoice(results.size());

        BookBean selectedBook = results.get(choice - 1);

        InOut.printLine("1) Borrow");
        InOut.printLine("2) Remove");

        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);

        switch (op) {
            case '1' -> {
                BorrowBookCLController borrowBookCLController = new BorrowBookCLController();
                borrowBookCLController.borrowABook(selectedBook);
            }
            case '2' -> {
                controller.deleteFromReadingList(selectedBook);
                InOut.printLine("*** Book correctly deleted ***\n");

            }
            default -> InOut.printLine("Invalid condition");
        }
    }

    //permette scegliere lista in cui inserire libro
    public void addBookToAList(BookBean book) {
        InOut.printLine("*** Choose a list ***\n");

        InOut.printLine("1) Books I Liked");
        InOut.printLine("2) Want To Read");

        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);


        ReadingListController readingListController = new ReadingListController();
        FavoriteBean favoriteBean = new FavoriteBean();
        favoriteBean.setIsbn(book.getIsbn());

        switch (op) {
            case '1' -> favoriteBean.setListName("Books I liked");
            case '2' -> favoriteBean.setListName("Want to read");
            default -> InOut.printLine("Invalid condition");
        }


        readingListController.addToReadingList(favoriteBean);
        InOut.printLine("*** Book correctly added ***\n");
    }
}
