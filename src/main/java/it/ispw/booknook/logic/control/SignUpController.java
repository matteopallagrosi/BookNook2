package it.ispw.booknook.logic.control;

import it.ispw.booknook.logic.Encrypter;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.boundary.BcryptAdapter;
import it.ispw.booknook.logic.database.dao.UserDao;
import it.ispw.booknook.logic.entity.Library;
import it.ispw.booknook.logic.entity.User;

public class SignUpController {

    public void registerUser(LoginBean loginBean) {
        User reader = User.getUser();
        //cifratura della password
        Encrypter encrypter = new BcryptAdapter();
        String encryptedHash = encrypter.encrypt(loginBean.getPassword());
        //setta i dettagli dell'utente e li salva sul db
        reader.setLogDetails(loginBean.getUsername(), loginBean.getEmail(), encryptedHash, loginBean.getType());
        reader.setImageProfile("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\account_circle_24dp.png");
        UserDao.registerReaderUser(reader);
    }

    public void registerLibrary(LibraryBean libraryDetails) {
        Library library = new Library();
        library.setUsername(libraryDetails.getUsername());
        library.setName(libraryDetails.getName());
        library.setAddress(libraryDetails.getAddress());
        library.setCity(libraryDetails.getCity());
        library.setOpeningTime(libraryDetails.opening());
        library.setClosingTime(libraryDetails.closing());
        //salva i dettagli della biblioteca sul db e crea un file su cui verranno salvate le review (delega alla Dao)
        UserDao.registerLibraryDetails(library);

    }
}
