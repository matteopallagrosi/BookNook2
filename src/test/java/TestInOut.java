import it.ispw.booknook.logic.boundary.secondary_view.InOut;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestInOut {

    @Test
    void testisIntegerDoubleInput() {
        assertFalse(InOut.isInteger("4.5", 10));
    }

    @Test
    void testisIntegerInvalidFormat() {
        assertFalse(InOut.isInteger("test", 10));
    }
}
