package java.org.example;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Bank {
    private int id ;
    private String BankName ;
    private String Country ;
    private List<Account> AccountList;

    public int getId()  {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        try {
            int clientId = dao.getBankIdByOtherInfos(this);
            if (clientId == -1) {
                System.out.println("Client not found.");
            }
            return clientId;
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
            return -1; // Or handle the error as needed
        }
    }
    // Method to add a bank
    public void addBank() throws SQLException {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        dao.addBank(this);
    }

    // Method to get all banks
    public static List<Bank> getAllBanks() throws SQLException {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        return dao.getAllBanks();
    }

    public Bank getBankinfosByid(int id) throws SQLException {
        Main.DatabaseDAO dao = new Main.DatabaseDAO();
        return dao.getBankById(id);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Method to create Client from JSON
    public static Bank fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Bank.class);
    }
}