package it.ispw.booknook.logic.control;

import it.ispw.booknook.logic.Encrypter;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.boundary.BcryptAdapter;
import it.ispw.booknook.logic.database.dao.UserDao;
import it.ispw.booknook.logic.entity.User;

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

    public void changeEmail(LoginBean oldDetails, LoginBean newDetails) {
        if (!newDetails.getEmail().equals(oldDetails.getEmail())) {
            UserDao.updateEmail(oldDetails.getEmail(), newDetails.getEmail());
        }
    }

    public void changePassword(LoginBean newDetails) {
        Encrypter encrypter = new BcryptAdapter();
        String encryptedHash = encrypter.encrypt(newDetails.getPassword());
        UserDao.updatePassword(User.getUser().getUsername(), encryptedHash);
        User.getUser().setPassword(encryptedHash);
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
                    UserDao.getProfile(username);  //potrebbero essere ancora nulli
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

    public void changeProfileDetails(LoginBean newProfileDetails) {
        String newName = newProfileDetails.getFirstName();
        String newLastName = newProfileDetails.getLastName();
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
        UserDao.updateProfile();
    }

    public void updateImageProfile(LoginBean newProfile) {
        User.getUser().setImageProfile(newProfile.getImageUrl());
        UserDao.saveProfileImage(User.getUser());
    }

    public void deleteAccount() {
        User userToDelete = User.getUser();
        UserDao.deleteProfile(userToDelete);
        User.deleteUser();
    }
}
