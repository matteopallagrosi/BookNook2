import it.ispw.booknook.logic.bean.LoginBean;
import it.ispw.booknook.logic.control.LoginController;
import it.ispw.booknook.logic.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TestLoginController {

    @Test
    void testCheckUserLoggedInvalidCredentials() {
        LoginBean loginDetails = new LoginBean();
        loginDetails.setEmail("admin@example.com");
        loginDetails.setPassword("Pippo.1235");
        LoginController loginController =  new LoginController();
        assertThrows(UserNotFoundException.class, ()->loginController.checkUserLogged(loginDetails));
    }
}
