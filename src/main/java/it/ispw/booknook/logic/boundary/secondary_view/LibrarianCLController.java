package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.AddBookController;
import it.ispw.booknook.logic.control.LoginController;

import java.util.ArrayList;
import java.util.List;

public class LibrarianCLController {

    public void startLibrarianCL() {

        while (true) {

            InOut.printLine("*** What should I do for you? ***\n");

            InOut.printLine("1) Add a book");

            InOut.printLine("2) Read reviews");

            InOut.printLine("3) Quit");

            char[] options = new char[]{'1', '2', '3'};

            char op = InOut.multiChoice(options, 3);

            switch (op) {
                case '1' -> addABook();
                case '2' -> readReviews();
                case '3' -> {
                    InOut.printLine("Bye bye!");
                    System.exit(0);
                }
                default -> {
                    InOut.printLine("Invalid condition");
                    return;
                }
            }
        }
    }

    private void addABook() {
        InOut.printLine("*** Add a new book to your library ***\n");
        String title = "";
        String author = "";
        String publisher = "";
        String year = "";
        String isbn = "";
        String numberCopies = "";

        while (title.isEmpty() || author.isEmpty()) {
            InOut.print("Title: ");
            title = InOut.readLine();
            InOut.print("Author: ");
            author = InOut.readLine();
        }

        while (publisher.isEmpty()) {
            InOut.print("Publisher: ");
            publisher = InOut.readLine();
        }

        while (year.isEmpty()) {
            InOut.print("Publication year: ");
            year = InOut.readLine();
        }

        while (isbn.length() != 13) {
            InOut.print("ISBN: ");
            isbn = InOut.readLine();
        }

        while (numberCopies.isEmpty() || !InOut.isInteger(numberCopies, 10) || Integer.parseInt(numberCopies) == 0) {
            InOut.print("Number of copies: ");
            numberCopies = InOut.readLine();
        }

        InOut.printLine("*** Is your book for consultation only? ***\n");
        InOut.printLine("1) Yes");
        InOut.printLine("2) No");

        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);

        boolean consultable = switch (op) {
            case '1' -> true;
            case '2' -> false;
            default -> throw new IllegalStateException("Unexpected value: " + op);
        };

        List<String> selectedTags = new ArrayList<>();
        AddBookController addBookController = new AddBookController();

        if (!consultable) {
            InOut.printLine("*** Choose tags ***\n");

            boolean state;
            List<Integer> choices = new ArrayList<>();
            do {

                BookBean bookBean = addBookController.calculateAvailableTags();
                List<String> tags = bookBean.getTags();
                for (int i = 1; i <= tags.size(); i++) {
                    InOut.printLine(i + ") " + tags.get(i - 1));
                }

                int choice = InOut.multiIntChoice(tags.size());

                if (!choices.contains(choice)) {
                    String selectedTag = tags.get(choice - 1);
                    selectedTags.add(selectedTag);
                    choices.add(choice);
                }

                InOut.printLine("*** Add more? ***\n");
                InOut.printLine("1) Yes");
                InOut.printLine("2) No");

                char op2 = InOut.multiChoice(options, 2);

                state = switch (op2) {
                    case '1' -> true;
                    case '2' -> false;
                    default -> throw new IllegalStateException("Unexpected value: " + op);
                };

            } while (state);
        }
        
        BookBean book = new BookBean(title,author, publisher, year, isbn, numberCopies);
        book.setTags(selectedTags);
        book.setForConsultation(consultable);
        
        addBookController.addABook(book);

        InOut.printLine("*** Book correctly added ***\n");
    }

    private void readReviews() {
        LoginController loginController = new LoginController();
        LoginBean loginDetails = loginController.getCurrentUsername();
        LibraryBean libraryDetails = new LibraryBean();
        libraryDetails.setUsername(loginDetails.getUsername());
        ReviewCLController reviewCLController = new ReviewCLController();
        reviewCLController.showReviews(libraryDetails);
    }






}
