
import java.org.example.Account;
import java.org.example.Bank;
import java.org.example.Client;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void testAccountInitialization() {
        Client client = new Client(0,"Alice", "Smith", "alice.smith@example.com", "5554443333", "789 Elm St",new ArrayList<>());
        Bank bank = new Bank(0,"Main Bank", "Country A",new ArrayList<>());

        Account account = new Account(0,new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),"Savings",0, client, bank,new ArrayList<>());
        assertEquals(new Date(System.currentTimeMillis()),account.getDate_creation());
        assertEquals(new Date(System.currentTimeMillis()),account.getDate_update());
        assertEquals(0,account.getBalance());
        assertEquals("Savings", account.getAccount_type());
        assertEquals(client, account.getClient());
        assertEquals(bank, account.getBank());
    }
}