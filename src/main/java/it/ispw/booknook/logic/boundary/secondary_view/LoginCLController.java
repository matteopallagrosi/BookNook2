package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.exception.UserNotFoundException;

public class LoginCLController {

    public void getUserCredentials() {
        InOut.printLine("*** Insert your credentials ***");

        String email = "";
        String password = "";

        while (email.isEmpty()) {
            InOut.print("Email: ");
            email = InOut.readLine();
        }
        while (password.isEmpty()) {
            InOut.print("Password: ");
            password = InOut.readLine();
        }

        LoginBean login = new LoginBean(email, password);
        LoginController controller = new LoginController();
        try {
            controller.checkUserLogged(login);
            //se utente lettore apre homepage per reader
            if (controller.isUserReader()) {
                //apre hompepage
                ReaderCLController readerCLController = new ReaderCLController();
                readerCLController.startReaderCL();
            }
            //se utente bibliotecario apre interfaccia bibliotecario
            else {
                LibrarianCLController librarianCLController = new LibrarianCLController();
                librarianCLController.startLibrarianCL();
            }
        } catch (UserNotFoundException e) {
            //altrimenti mostra messaggio d'errore
            InOut.printLine(e.getMessage());
        }
    }
}
