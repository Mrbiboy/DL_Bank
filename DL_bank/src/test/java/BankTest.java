
import java.org.example.Bank;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    @Test
    void testBankInitialization() {
        Bank bank = new Bank(0,"Main Bank", "Country B",new ArrayList<>());

        assertEquals("Main Bank", bank.getBankName());
        assertEquals("Country B", bank.getCountry());
    }
}
