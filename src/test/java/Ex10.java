import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10 {

    @Test
    public void simpleTest() {
        String str = "fghhhhjhsdjasdfg";
        System.out.println(str.length());
        assertTrue(str.length() > 15, "The string length is less than sixteen");
    }


}
