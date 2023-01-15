package it.ispw.booknook.logic.control;

import at.favre.lib.crypto.bcrypt.BCrypt;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.database.dao.ReaderUserDao;
import it.ispw.booknook.logic.entity.User;

import java.sql.SQLException;

public class SettingsController {

    public LoginBean processUserDetails() {
        LoginBean loginBean = new LoginBean();
        if (new LoginController().verifyLogin()) {
            String email = User.getUser().getEmail();
            String password = User.getUser().getPassword();
            loginBean.setEmail(email);
            loginBean.setPassword(password);
        }
        return loginBean;
    }

    public void changeEmail(LoginBean oldDetails, LoginBean newDetails) throws SQLException {
        if (!newDetails.getEmail().equals(oldDetails.getEmail())) {
            ReaderUserDao.updateEmail(oldDetails.getEmail(), newDetails.getEmail());
        }
    }

    public void changePassword(LoginBean newDetails) throws SQLException {
        String newPassword = BCrypt.withDefaults().hashToString(12, newDetails.getPassword().toCharArray());
        ReaderUserDao.updatePassword(User.getUser().getUsername(), newPassword);
        User.getUser().setPassword(newPassword);
    }

    public LoginBean processProfileDetails() {
        LoginBean profileDetails = new LoginBean();
        if (new LoginController().verifyLogin()) {
            //prova a recuperare nome e cognome dall'istanza di user se presenti
            String username = User.getUser().getUsername();
            String firstName = User.getUser().getFirstName();
            String lastName = User.getUser().getLastName();
            String address = User.getUser().getAddress();
            String city = User.getUser().getCity();
            String zip = User.getUser().getZip();
            String country = User.getUser().getCountry();
            //altrimenti li recupera dal db
            if (firstName == null || lastName == null || address == null || city == null || zip == null || country == null) {
                try {
                    ReaderUserDao.getProfile(username);  //potrebbero essere ancora nulli
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //setta nome e cognome, immagine profilo e indirizzo spedizione
            profileDetails.setUsername(username);
            profileDetails.setFirstName(User.getUser().getFirstName());
            profileDetails.setLastName(User.getUser().getLastName());
            profileDetails.setAddress(User.getUser().getAddress());
            profileDetails.setCity(User.getUser().getCity());
            profileDetails.setZip(User.getUser().getZip());
            profileDetails.setCountry(User.getUser().getCountry());
            profileDetails.setProfileImage(User.getUser().getImageProfile());
        }
        //se l'utente non è loggato non ci sono dati disponibili tranne un logo per il profilo di default
        return profileDetails;
    }

    public void changeProfileDetails(LoginBean newProfileDetails) throws SQLException {
        String newName = newProfileDetails.getFirstName();
        String newLastName = newProfileDetails.getLastName();
        String username =  User.getUser().getUsername();
        String newAddress = newProfileDetails.getAddress();
        String newCity = newProfileDetails.getCity();
        String newZip = newProfileDetails.getZip();
        String newCountry = newProfileDetails.getCountry();
        User.getUser().setFirstName(newName);
        User.getUser().setLastName(newLastName);
        User.getUser().setAddress(newAddress);
        User.getUser().setCity(newCity);
        User.getUser().setZip(newZip);
        User.getUser().setCountry(newCountry);
        ReaderUserDao.updateProfile(username, newName, newLastName, newAddress, newCity, newZip, newCountry);
    }

    public void updateImageProfile(LoginBean newProfile) {
        User.getUser().setImageProfile(newProfile.getImageUrl());
        ReaderUserDao.saveProfileImage(User.getUser());
    }

    public void deleteAccount() {
        User userToDelete = User.getUser();
        ReaderUserDao.deleteProfile(userToDelete);
        User.deleteUser();
    }
}
