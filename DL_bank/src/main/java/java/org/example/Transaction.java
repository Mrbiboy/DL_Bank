
package java.org.example;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Transaction {
    private int id;
    private TransactionType type;
    private double amount;
    private Date date_transaction;
    private String description;
    private Account account_debit;
    private Account account_credit;

    public enum TransactionType {
        VIRINT,   // Same bank
        VIREST,   // Different banks, same country
        VIRMULTA  // Different banks, different countries
    }
    // Method to add a transaction
    public void addTransaction() throws SQLException {
        this.type = determineTransactionType();
        Main.DAO dao = new Main.DAO();
        dao.addTransaction(this);
    }
    private TransactionType determineTransactionType()  {
        if (account_debit.getBank().getId() == account_credit.getBank().getId()) {
            // Same bank
            return TransactionType.VIRINT;
        } else if (account_debit.getBank().getCountry().equals(account_credit.getBank().getCountry())) {
            // Different banks, same country
            return TransactionType.VIREST;
        } else {
            // Different banks, different countries
            return TransactionType.VIRMULTA;
        }
    }

    public TransactionType getType()  {
        return this.determineTransactionType();
    }

    // Method to get all transactions
    public static List<Transaction> getAllTransactions() throws SQLException {
        Main.DAO dao = new Main.DAO();
        return dao.getAllTransactions();
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

