package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.control.SettingsController;
import it.ispw.booknook.logic.exception.UserNotFoundException;

public class SettingsCLController {

    public void manageSettings() {

        while(true) {
            InOut.printLine("*** Manage your settings account ***\n");

            InOut.printLine("1) Account settings");
            InOut.printLine("2) Profile settings");
            InOut.printLine("3) Delete account");
            InOut.printLine("4) Back to home");

            char[] options = new char[]{'1', '2', '3', '4'};

            char op = InOut.multiChoice(options, 4);

            SettingsController settingsController = new SettingsController();


            switch (op) {
                case '1' -> manageAccountSettings();

                case '2' -> manageProfileSettings();
                case '3' -> deleteAccount(settingsController);
                case '4' -> {
                    InOut.printLine("*** Back to home ***\n");
                    return;
                }
                default -> InOut.printLine("Invalid condition");
            }
        }
    }


    private void manageAccountSettings() {

        InOut.printLine("*** Change your email ***\n");

        String email = "";

        while (email.isEmpty()) {
            InOut.print("Email: ");
            email = InOut.readLine();
        }

        SettingsController settingsController = new SettingsController();
        LoginBean oldDetails = settingsController.processUserDetails();
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(email);
        if (loginBean.getEmail() == null) {  //validazione sintattica
            InOut.printLine("Invalid email\n");
            return;
        }

        settingsController.changeEmail(oldDetails, loginBean);
        LoginController loginController = new LoginController();
        loginController.updateUserEmail(loginBean);

        InOut.printLine("*** Email successfully changed ***\n");

        InOut.printLine("*** Change your password ***\n");

        String oldPassword = "";

        while (oldPassword.isEmpty()) {
            InOut.print("Old password: ");
            oldPassword = InOut.readLine();
        }

        oldDetails.setPlainPassword(oldPassword);
        //se old password è sbagliata mostra errore
        try {
            loginController.checkUserLogged(oldDetails);
        } catch (UserNotFoundException e) {
            InOut.printLine("Wrong password\n");
            return;
        }

        InOut.printLine("*** New password must be over 8 characters included numbers and special characters ( . _ - ? ! ) ***");
        String newPassword = "";

        while (newPassword.isEmpty()) {
            InOut.print("New password: ");
            newPassword = InOut.readLine();
        }

        loginBean.setPassword(newPassword);
        if (loginBean.getPassword() == null) {
            InOut.printLine("Password does not match the requirements");
            return;
        }

        settingsController.changePassword(loginBean);

        InOut.printLine("*** Password successfully updated ***");
    }

    private void manageProfileSettings() {

        InOut.printLine("*** Change your profile details ***\n");
        String firstName = "";
        String lastName = "";

        while (firstName.isEmpty()) {
            InOut.print("First name: ");
            firstName = InOut.readLine();
        }
        while (lastName.isEmpty()) {
            InOut.print("Last name: ");
            lastName = InOut.readLine();
        }

        InOut.printLine("*** Change your Billing Address ***\n");
        String billingStreet = "";
        String billingCountry = "";
        String billingZip = "";
        String billingCity = "";

        while (billingStreet.isEmpty()) {
            InOut.print("Street: ");
            billingStreet = InOut.readLine();
        }
        while (billingCountry.isEmpty()) {
            InOut.print("Country: ");
            billingCountry = InOut.readLine();
        }
        while (billingZip.isEmpty()) {
            InOut.print("Zip: ");
            billingZip = InOut.readLine();
        }
        while (billingCity.isEmpty()) {
            InOut.print("City: ");
            billingCity = InOut.readLine();
        }

        LoginBean loginBean = new LoginBean(firstName, lastName, billingStreet, billingCountry, billingZip, billingCity);

        SettingsController settingsController = new SettingsController();
        settingsController.changeProfileDetails(loginBean);

        InOut.printLine("*** Profile settings correctly changed ***\n");
    }



    public void deleteAccount(SettingsController settingsController) {

        InOut.printLine("*** Are you sure? ***\n");

        InOut.printLine("1) Yes");
        InOut.printLine("2) No");

        char[] options = new char[]{'1', '2'};

        char op = InOut.multiChoice(options, 2);

        switch (op) {
            case '1' -> {
                settingsController.deleteAccount();
                InOut.printLine("*** Account correctly deleted ***\n");
                InOut.printLine("*** Bye bye ***\n");
                System.exit(1);
            }
            case '2' -> {
                InOut.printLine("*** Back to settings ***\n");
            }
            default -> {
                InOut.printLine("Invalid condition");
            }
        }
    }
}
