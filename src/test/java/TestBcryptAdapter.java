import it.ispw.booknook.logic.boundary.BcryptAdapter;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;

class TestBcryptAdapter {

    @Test
    void testVerify() {
        BcryptAdapter bcrypt = new BcryptAdapter();
        String hashString = bcrypt.encrypt("test");
        assertTrue(bcrypt.verify("test", hashString));
    }
}
