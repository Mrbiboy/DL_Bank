
package java.org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private MySQLConnector connector;
    private Connection connection;

    public DAO()  {
        try {
            connector = new MySQLConnector();
            connection = connector.connect();
            connection.setAutoCommit(false); // Disable auto-commit
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() throws SQLException {
        connection.commit(); // Commit transaction
    }

    public void rollback() throws SQLException {
        connection.rollback(); // Rollback transaction
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close(); // Close connection
        }
    }


    // Add a new client
    public void addClient(Client client) throws SQLException {
        String query = "INSERT INTO clients (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());
            stmt.setString(5, client.getAddress());
            stmt.executeUpdate();
        }
    }


    // Get all clients
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setFirstName(rs.getString("first_name"));
                client.setLastName(rs.getString("last_name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                clients.add(client);
            }
        }
        return clients;
    }

    // Add a new account

    // Add a method to get accounts by client ID
    public List<Account> getAccountsByClientId(int clientId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE client_id = ?";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setDate_creation(rs.getDate("date_creation"));
                    account.setDate_update(rs.getDate("date_update"));
                    account.setAccount_type(rs.getString("account_type"));
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }

    // Add a similar method for fetching accounts by bank ID if needed
    public List<Account> getAccountsByBankId(int bankId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE bank_id = ?";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bankId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setDate_creation(rs.getDate("date_creation"));
                    account.setDate_update(rs.getDate("date_update"));
                    account.setAccount_type(rs.getString("account_type"));
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }

    // Update the addAccount method to add the account to the client's and bank's accounts lists
    public void addAccount(Account account) throws SQLException {
        String query = "INSERT INTO accounts (date_creation, date_update, account_type , client_id, bank_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, account.getDate_creation());
            stmt.setDate(2, account.getDate_update());
            stmt.setString(3, account.getAccount_type());
            stmt.setInt(4, account.getClient().getId());
            stmt.setInt(5, account.getBank().getId());
            stmt.executeUpdate();

            // Get the generated account ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getInt(1));
                }
            }

            // Update the client's account list
            List<Account> clientAccounts = getAccountsByClientId(account.getClient().getId());
            account.getClient().setAccounts(clientAccounts);

            // Update the bank's account list
            List<Account> bankAccounts = getAccountsByBankId(account.getBank().getId());
            account.getBank().setAccountList(bankAccounts);
        }
    }
    public Account get_BalanceByid(int id) throws SQLException {
        String query = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setDate_creation(rs.getDate("date_creation"));
                    account.setDate_update(rs.getDate("date_update"));
                    account.setAccount_type(rs.getString("account_type"));
                    account.setBalance(rs.getInt("balance"));
                    account.setClient(getClientById(rs.getInt("client_id")));
                    account.setBank(getBankById(rs.getInt("bank_id")));
                    return account;
                }
            }
        }
        return null;
    }


    // Get all accounts
    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setDate_creation(rs.getDate("date_creation"));
                account.setDate_update(rs.getDate("date_update"));
                account.setAccount_type(rs.getString("account_type"));
                // Fetch the client and bank for this account
                Client client = getClientById(rs.getInt("client_id")); // You need to implement this method
                account.setClient(client);
                Bank bank = getBankById(rs.getInt("bank_id")); // You need to implement this method
                account.setBank(bank);
                accounts.add(account);
            }
        }
        return accounts;
    }
    public void addBalancetoAccount(Account account_debit,double a ) throws SQLException{
        String query="UPDATE accounts SET balance = balance + ? WHERE id = ?";
        try(Connection conn = connector.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, a);
            stmt.setInt(2, account_debit.getId());
            stmt.executeUpdate();
        }
        account_debit.setBalance(account_debit.getBalance()+a);
    }
    public void takeBalancefromAccount(Account account_credit,double a ) throws SQLException{
        String query="UPDATE accounts SET balance = balance - ? WHERE id = ?";
        try(Connection conn = connector.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, a);
            stmt.setInt(2, account_credit.getId());
            stmt.executeUpdate();
        }
        account_credit.setBalance(account_credit.getBalance()-a);

    }
    // Add a new transaction
    public void addTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions (type, amount, date_transaction, description, account_debit, account_credit) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connector.connect()) {
            // Insert the transaction record
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, transaction.getType().toString());
                stmt.setDouble(2, transaction.getAmount());
                stmt.setDate(3, transaction.getDate_transaction());
                stmt.setString(4, transaction.getDescription());
                stmt.setInt(5, transaction.getAccount_debit().getId());
                stmt.setInt(6, transaction.getAccount_credit().getId());
                stmt.executeUpdate();
            }

            // Update account balances
            addBalancetoAccount(transaction.getAccount_debit(), transaction.getAmount());
            takeBalancefromAccount(transaction.getAccount_credit(), transaction.getAmount());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Get all transactions
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setDate_transaction(rs.getDate("date_transaction"));
                transaction.setDescription(rs.getString("description"));
                // Fetch the accounts for this transaction
                Account accountDebit = getAccountById(rs.getInt("account_debit")); // You need to implement this method
                transaction.setAccount_debit(accountDebit);
                Account accountCredit = getAccountById(rs.getInt("account_credit")); // You need to implement this method
                transaction.setAccount_credit(accountCredit);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    // Add a new bank
    public void addBank(Bank bank) throws SQLException {
        String query = "INSERT INTO banks (Bank_name, country) VALUES (?, ?)";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, bank.getBankName());
            stmt.setString(2, bank.getCountry());
            stmt.executeUpdate();
        }
    }

    // Get all banks
    public List<Bank> getAllBanks() throws SQLException {
        List<Bank> banks = new ArrayList<>();
        String query = "SELECT * FROM banks";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Bank bank = new Bank();
                bank.setId(rs.getInt("id"));
                bank.setBankName(rs.getString("Bank_name"));
                bank.setCountry(rs.getString("Country"));
                banks.add(bank);
            }
        }
        return banks;
    }

    // Utility methods to get Client and Bank by ID
    public   Client getClientById(int id) throws SQLException {
        String query = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            new ArrayList<>()
                    );
                }
            }
        }
        return null; // Return null if client is not found
    }
    public int getClientIdByOtherInfos(Client client) throws SQLException {
        String query = "SELECT id FROM clients WHERE first_name= ? AND last_name=? AND email=? AND phone=? AND  address=?";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());
            stmt.setString(5, client.getAddress());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1 ; // Return null if client is not found
    }

    public int getBankIdByOtherInfos(Bank bank) throws SQLException {
        String query = "SELECT id FROM banks WHERE bank_name= ? AND country=? ";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, bank.getBankName());
            stmt.setString(2, bank.getCountry());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1 ; // Return null if client is not found
    }

    public int getAccountIdByOtherInfos(Account account) throws SQLException {
        String query = "SELECT id FROM accounts WHERE date_creation= ? AND date_update=? AND account_type=? AND client_id=? ";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, account.getDate_creation());
            stmt.setDate(2, account.getDate_update());
            stmt.setString(3, account.getAccount_type());
            stmt.setInt(4, account.getClient().getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1 ; // Return null if client is not found
    }

    public  Bank getBankById(int id) throws SQLException {
        String query = "SELECT * FROM banks WHERE id = ?";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Bank(
                            rs.getInt("id"),
                            rs.getString("Bank_name"),
                            rs.getString("Country"),
                            new ArrayList<>()
                    );
                }
            }
        }
        return null; // Return null if bank is not found
    }

    public  Account getAccountById(int id) throws SQLException {
        String query = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setDate_creation(rs.getDate("date_creation"));
                    account.setDate_update(rs.getDate("date_update"));
                    account.setBalance(rs.getDouble("balance"));
                    account.setAccount_type(rs.getString("account_type"));
                    account.setClient(getClientById(rs.getInt("client_id")));
                    account.setBank(getBankById(rs.getInt("bank_id")));
                    return account;
                }
            }
        }
        return null; // Return null if account is not found
    }
    public List<Client> searchClients(String query) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ? OR id = ? ";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setInt(4, Integer.parseInt(query));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client();
                    client.setId(rs.getInt("id"));
                    client.setFirstName(rs.getString("first_name"));
                    client.setLastName(rs.getString("last_name"));
                    client.setEmail(rs.getString("email"));
                    client.setPhone(rs.getString("phone"));
                    client.setAddress(rs.getString("address"));
                    clients.add(client);
                }
            }
        }
        return clients;
    }

    public List<Account> searchAccounts(String query) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE account_type LIKE ? OR id = ?";
        try (Connection conn = connector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setInt(2, Integer.parseInt(query));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setDate_creation(rs.getDate("date_creation"));
                    account.setDate_update(rs.getDate("date_update"));
                    account.setBalance(rs.getDouble("balance"));
                    account.setClient(getClientById(rs.getInt("client_id")));
                    account.setBank(getBankById(rs.getInt("bank_id")));
                    account.setAccount_type(rs.getString("account_type"));
                    // Set other fields as necessary
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }

}

