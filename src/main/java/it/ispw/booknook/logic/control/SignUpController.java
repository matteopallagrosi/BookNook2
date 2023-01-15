package it.ispw.booknook.logic.control;

import at.favre.lib.crypto.bcrypt.BCrypt;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.database.dao.ReaderUserDao;
import it.ispw.booknook.logic.entity.User;
import it.ispw.booknook.logic.entity.UserType;

public class SignUpController {

    public void registerReader(LoginBean loginBean) {
        User reader = User.getUser();
        reader.setLogDetails(loginBean.getUsername(), loginBean.getEmail(), BCrypt.withDefaults().hashToString(12, loginBean.getPassword().toCharArray()), UserType.READER);
        reader.setImageProfile("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\account_circle_24dp.png");
        ReaderUserDao.registerReaderUser(reader);

    }
}
