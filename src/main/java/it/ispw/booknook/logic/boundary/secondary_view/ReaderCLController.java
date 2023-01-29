package it.ispw.booknook.logic.boundary.secondary_view;

public class ReaderCLController {

    public void startReaderCL() {

        while (true){

            InOut.printLine("*** What should I do for you? ***\n");

            InOut.printLine("1) Borrow a book by title or author");

            InOut.printLine("2) Borrow a book by tag");

            InOut.printLine("3) Reserve a consultation");

            InOut.printLine("4) Manage your reading lists");

            InOut.printLine("5) Manage account settings");

            InOut.printLine("6) Quit");

            char[] options = new char[]{'1', '2', '3', '4', '5', '6'};

            char op = InOut.multiChoice(options, 6);

            switch (op) {
                case '1' -> {
                    BorrowBookCLController borrowBookCLController = new BorrowBookCLController();
                    borrowBookCLController.borrowBookByName();
                }
                case '2' -> {
                    BorrowBookCLController borrowBookCLController = new BorrowBookCLController();
                    borrowBookCLController.borrowBookByTag();
                }
                case '3' -> {
                    ConsultationCLController consultationCLController = new ConsultationCLController();
                    consultationCLController.reserveConsultation();
                }
                case '4' -> {
                    ReadingListCLController readingListCLController = new ReadingListCLController();
                    readingListCLController.manageReadingList();

                }
                case '5' -> {
                    SettingsCLController settingsCLController = new SettingsCLController();
                    settingsCLController.manageSettings();
                }
                case '6' -> {
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
}
