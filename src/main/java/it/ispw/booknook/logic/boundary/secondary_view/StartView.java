package it.ispw.booknook.logic.boundary.secondary_view;

public class StartView {

    public void startCLView() {
        InOut.printLine("Welcome to BookNook!\n");


        while(true) {
            InOut.printLine("*** What should I do for you? ***\n");

            InOut.printLine("1) Login");

            InOut.printLine("2) Signup");

            InOut.printLine("3) Quit");

            char[] options = new char[]{'1', '2', '3'};

            char op = InOut.multiChoice(options, 3);

            switch (op) {
                case '1' -> {
                    LoginCLController controller = new LoginCLController();
                    controller.getUserCredentials();
                }
                case '2' -> {
                    SignupCLController signupCLController = new SignupCLController();
                    signupCLController.registerNewUser();
                }
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
}
