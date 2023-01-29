package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.SignUpController;

public class SignupCLController {

    public void registerNewUser() {
        InOut.printLine("\n*** Which type of user are you? ***\n");

        InOut.printLine("1) Reader");

        InOut.printLine("2) Librarian");

        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);

        switch (op) {
            case '1' -> registerReader();
            case '2' -> registerLibrarian();
            default -> InOut.printLine("Invalid condition");
        }
    }

    private void registerReader() {
        InOut.printLine("\n*** Register your credentials as a Reader ***\n");

        String email = "";
        String username = "";
        String password = "";

        //verifica che i campi non siano vuoti
        while (email.isEmpty()) {
            InOut.print("Email: ");
            email = InOut.readLine();
        }
        while (username.isEmpty()) {
            InOut.print("Username: ");
            username = InOut.readLine();
        }
        while (password.isEmpty()) {
            InOut.printLine("*** Password must be over 8 characters included numbers and special characters ( . _ - ? ! ) ***");
            InOut.print("Password: ");
            password = InOut.readLine();
        }

        //il bean effettua controllo sintattico
        LoginBean registration = new LoginBean(email, username, password);
        if (registration.getEmail() == null) {
            InOut.print("Email is incorrect\n");
            return;
        }
        if (registration.getPassword() == null) {
            InOut.printLine("Password does not match requirements");
            return;
        }
        registration.setIsReader(true);
        SignUpController controller = new SignUpController();
        controller.registerUser(registration);

        InOut.printLine("Your registration has been completed successfully. Have fun!\"");
        ReaderCLController readerCLController = new ReaderCLController();
        readerCLController.startReaderCL();
    }

    private void registerLibrarian() {
        InOut.printLine("\n*** Register your credentials as a Librarian ***\n");

        String email = "";
        String username = "";
        String password = "";
        String name = "";
        String address = "";
        String city = "";
        String openingHours = "";
        String closingHours = "";

        //verifica che i campi non siano vuoti
        while (email.isEmpty()) {
            InOut.print("Email: ");
            email = InOut.readLine();
        }
        while (username.isEmpty()) {
            InOut.print("Username: ");
            username = InOut.readLine();
        }
        while (password.isEmpty()) {
            InOut.printLine("*** Password must be over 8 characters included numbers and special characters ( . _ - ? ! ) ***");
            InOut.print("Password: ");
            password = InOut.readLine();
        }
        InOut.printLine("\n*** Insert your library details ***\n");
        while (name.isEmpty()) {
            InOut.print("Library name: ");
            name = InOut.readLine();
        }
        while (address.isEmpty()) {
            InOut.print("Address: ");
            address = InOut.readLine();
        }
        while (city.isEmpty()) {
            InOut.print("City: ");
            city = InOut.readLine();
        }
        while (openingHours.isEmpty()) {
            InOut.print("Opening hours[hh:mm]: ");
            openingHours = InOut.readLine();
        }
        while (closingHours.isEmpty()) {
            InOut.print("Closing hours[hh:mm]: ");
            closingHours = InOut.readLine();
        }
        //il bean effettua controllo sintattico
        LoginBean registration = new LoginBean(email, username, password);
        if (registration.getEmail() == null) {
            InOut.print("Email is incorrect\n");
            return;
        }
        if (registration.getPassword() == null) {
            InOut.printLine("Password does not match requirements");
            return;
        }
        registration.setIsReader(false);

        LibraryBean libraryDetails = new LibraryBean(username, name, address, city, openingHours, closingHours);

        //il bean effettua controllo sintattico su orario
        if (!libraryDetails.checkOpeningTimeFormat() || !libraryDetails.checkClosingTimeFormat()) {
            InOut.printLine("Wrong time format");
            return;
        }
        if (libraryDetails.closing().toLocalTime().isBefore(libraryDetails.opening().toLocalTime())){
            InOut.printLine("Opening time is after closing time");
            return;
        }

        //se corrette registra utente (inserisce dati sul db), chiamando controller applicativo
        SignUpController controller = new SignUpController();
        controller.registerUser(registration);

        //invoca controller per registrazione dati libreria
        controller.registerLibrary(libraryDetails);

        InOut.printLine("Your registration has been completed successfully. Have fun!\"");
        LibrarianCLController librarianCLController = new LibrarianCLController();
        librarianCLController.startLibrarianCL();
    }
}
