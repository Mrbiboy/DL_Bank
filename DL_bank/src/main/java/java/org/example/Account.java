package java.org.example;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Account {
    private int id;
    private Date Date_creation ;
    private Date Date_update ;
    private String account_type;
    private double balance;
    private Client client;
    private Bank bank;
    private List<Transaction> transactions;


    public int getId()  {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        try {
            int accountID = dao.getAccountIdByOtherInfos(this);
            if (accountID == -1) {
                System.out.println("Client not found.");
            }
            return accountID;
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
            return -1; // Or handle the error as needed
        }
    }

    // Method to add an account
    public void addAccount() throws SQLException {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        dao.addAccount(this);
    }

    // Method to get all accounts
    public static List<Account> getAllAccounts() throws SQLException {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        return dao.getAllAccounts();
    }
    public Account getAccountinfosByid() throws SQLException {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        return dao.getAccountById(id);
    }
    public void setBalance(double amount) throws SQLException {
        this.balance = amount;
    }



    // Method to convert Transaction to JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Method to create Transaction from JSON
    public static Transaction fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Transaction.class);
    }
}
