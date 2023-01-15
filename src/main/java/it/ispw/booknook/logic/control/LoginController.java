package it.ispw.booknook.logic.control;

import at.favre.lib.crypto.bcrypt.BCrypt;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.database.dao.ReaderUserDao;
import it.ispw.booknook.logic.entity.User;

public class LoginController {

    public boolean checkUserLogged(LoginBean loginBean) {
        String userPass = null;
        try {
            userPass = ReaderUserDao.getPassUser(loginBean.getEmail());
            BCrypt.Result result = BCrypt.verifyer().verify(loginBean.getPassword().toCharArray(), userPass);
            if (result.verified) {
                //crea l'istanza di utente loggato
                ReaderUserDao.getReaderUser(loginBean.getEmail());
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
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


}
