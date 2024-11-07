
import java.org.example.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class daotest {
    private DAO databaseDAO;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        databaseDAO = new DAO();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback(); // Rollback any changes made during the test
        connection.close(); // Close the connection
    }

    @Test
    void testAddAndRetrieveClient() throws SQLException {
        Client client = new Client(0,"ANAS", "TAHALLI", "anas.thl@example.com", "10203040", "789 Elm St",new ArrayList<>());
        databaseDAO.addClient(client);

        int clientId = databaseDAO.getClientIdByOtherInfos(client);
        assertNotEquals(-1, clientId);

        Client retrievedClient = databaseDAO.getClientById(clientId);
        assertNotNull(retrievedClient);
        assertEquals("ANAS", retrievedClient.getFirstName());
        assertEquals("TAHALLI", retrievedClient.getLastName());
        assertEquals("anas.thl@example.com", retrievedClient.getEmail());
        assertEquals("10203040", retrievedClient.getPhone());
        assertEquals("789 Elm St", retrievedClient.getAddress());

    }

    @Test
    void testAddAndRetrieveAccount() throws SQLException {
        Client client = new Client(0,"ANAS", "thl", "anas.thhhh@example.com", "789953333", "789 Elm St",new ArrayList<>());
        databaseDAO.addClient(client);
        client.setId(databaseDAO.getClientIdByOtherInfos(client));

        Bank bank = new Bank(0,"Main Bank", "Country A",new ArrayList<>());
        databaseDAO.addBank(bank);
        bank.setId(databaseDAO.getBankIdByOtherInfos(bank));

        Account account = new Account(0,new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),"Savings",0, client, bank,new ArrayList<>());
        databaseDAO.addAccount(account);

        int accountId = databaseDAO.getAccountIdByOtherInfos(account);
        Account retrievedAccount = databaseDAO.getAccountById(accountId);

        assertNotNull(retrievedAccount);
        assertEquals(accountId, retrievedAccount.getId());
        assertEquals(new Date(System.currentTimeMillis()),retrievedAccount.getDate_creation());
        assertEquals(new Date(System.currentTimeMillis()),retrievedAccount.getDate_update());
        assertEquals("Savings", retrievedAccount.getAccount_type());
        assertEquals(client.getId(), retrievedAccount.getClient().getId());
        assertEquals(bank.getId(), retrievedAccount.getBank().getId());
    }

    @Test
    void testAddTransaction() throws SQLException {

        Client clientDebit = new Client(0,"Alice", "Smith", "alice.smith@example.com", "5554443333", "789 Elm St",new ArrayList<>());
        Client clientCredit = new Client(1,"Bob", "Jones", "bob.jones@example.com", "444555666", "101 Maple Ave",new ArrayList<>());
        databaseDAO.addClient(clientDebit);
        databaseDAO.addClient(clientCredit);
        clientDebit.setId(databaseDAO.getClientIdByOtherInfos(clientDebit));
        clientCredit.setId(databaseDAO.getClientIdByOtherInfos(clientCredit));

        Bank bank = new Bank(0,"Main Bank", "Country B",new ArrayList<>());
        databaseDAO.addBank(bank);
        bank.setId(databaseDAO.getBankIdByOtherInfos(bank));

        Account debitAccount = new Account(0,new Date(System.currentTimeMillis()) ,new Date(System.currentTimeMillis()),"Checking",0,clientDebit, bank,new ArrayList<>());
        Account creditAccount = new Account(1,new Date(System.currentTimeMillis()) ,new Date(System.currentTimeMillis()),"Checking",0,clientCredit, bank,new ArrayList<>());
        databaseDAO.addAccount(debitAccount);
        databaseDAO.addAccount(creditAccount);


        Transaction transaction = new Transaction(0,"Transfer", 150.0,new Date(System.currentTimeMillis()), "Payment for services", debitAccount, creditAccount);
        databaseDAO.addTransaction(transaction);

        List<Transaction> transactions = databaseDAO.getAllTransactions();
        assertFalse(transactions.isEmpty());
        Transaction lastTransaction = transactions.get(transactions.size() - 1);
        assertEquals("Transfer", lastTransaction.getType());
        assertEquals(150.0, lastTransaction.getAmount());
        assertEquals(new Date(System.currentTimeMillis()),lastTransaction.getDate_transaction());
        assertEquals("Payment for services", lastTransaction.getDescription());
        assertEquals(debitAccount.getId(),lastTransaction.getAccount_debit().getId());
        assertEquals(creditAccount.getId(),lastTransaction.getAccount_credit().getId());

    }
}
