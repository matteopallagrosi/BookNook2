import it.ispw.booknook.logic.boundary.CardValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestCardValidator {

    @Test
    void testValidityCheckValidNUmber() {
        assertTrue(CardValidator.validitychek(5359214244487883L));
    }

    @Test
    void testValidityCheckInvalidNumber() {
        assertFalse(CardValidator.validitychek(1325645634568646L));
    }
}
