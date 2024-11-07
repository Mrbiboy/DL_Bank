

import java.org.example.Account;
import java.org.example.Bank;
import java.org.example.Client;
import java.org.example.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test
    void testTransactionInitialization()throws Exception {
        Client clientDebit = new Client(0,"Alice", "Smith", "alice.smith@example.com", "5554443333", "789 Elm St",new ArrayList<>());
        Client clientCredit = new Client(1,"Bob", "Jones", "bob.jones@example.com", "444555666", "101 Maple Ave",new ArrayList<>());
        Bank bank = new Bank(0,"Main Bank", "Country A",new ArrayList<>());

        Account debitAccount = new Account(0,new Date(System.currentTimeMillis()) ,new Date(System.currentTimeMillis()),"Checking",0,clientDebit, bank,new ArrayList<>());
        Account creditAccount = new Account(1,new Date(System.currentTimeMillis()) ,new Date(System.currentTimeMillis()),"Checking",0,clientCredit, bank,new ArrayList<>());

        Transaction transaction = new Transaction(0, Transaction.TransactionType.VIRINT, 150.0,new Date(System.currentTimeMillis()), "Payment for services", debitAccount, creditAccount);

        assertEquals(Transaction.TransactionType.VIRINT, transaction.getType());
        assertEquals(150.0, transaction.getAmount());
        assertEquals(new Date(System.currentTimeMillis()),transaction.getDate_transaction());
        assertEquals("Payment for services", transaction.getDescription());
        assertEquals(debitAccount,transaction.getAccount_debit());
        assertEquals(creditAccount,transaction.getAccount_credit());

    }
}