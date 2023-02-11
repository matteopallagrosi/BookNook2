package it.ispw.booknook.logic.control;

import it.ispw.booknook.logic.Encrypter;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.BcryptAdapter;
import it.ispw.booknook.logic.database.dao.UserDao;
import it.ispw.booknook.logic.entity.User;
import it.ispw.booknook.logic.entity.UserType;
import it.ispw.booknook.logic.exception.UserNotFoundException;

import java.sql.SQLException;

public class LoginController {

    public void checkUserLogged(LoginBean loginBean) throws UserNotFoundException {
        String userPass = null;
        try {
            //recupera l'hash della password dal db
            userPass = UserDao.getPassUser(loginBean.getEmail());
            //verica se la password inserita dall'utente corrisponde
            Encrypter encrypter = new BcryptAdapter();
            encrypter.verify(loginBean.getPassword(), userPass);
            if (encrypter.verify(loginBean.getPassword(), userPass)) {
                //crea l'istanza di utente loggato
                UserDao.getReaderUser(loginBean.getEmail());
            }
            else {
                throw new UserNotFoundException("Invalid email or password");
            }
        } catch (SQLException e) {
            //chaining
            throw new UserNotFoundException("Invalid email or password", e);
        }
    }

    public boolean verifyLogin() {
        return User.isLogged();
    }

    public void updateUserEmail(LoginBean loginBean) {
        if (verifyLogin()) {
            User.getUser().setEmail(loginBean.getEmail());
        }
    }

    public void updateUserPassword(LoginBean loginBean) {
        if (verifyLogin()) {
            User.getUser().setPassword(loginBean.getPassword());
        }
    }

    public boolean isUserReader() {
        return User.getUser().getType() == UserType.READER;
    }

    public LoginBean getCurrentUsername() {
        LoginBean loginDetails = new LoginBean();
        loginDetails.setUsername(User.getUser().getUsername());
        return loginDetails;
    }
}
