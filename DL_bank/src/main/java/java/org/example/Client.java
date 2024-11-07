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

public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String Address;
    private List<Account> accounts;
    public int getId()  {
        Main.DAO dao = new Main.DAO();
        try {
            int clientId = dao.getClientIdByOtherInfos(this);
            if (clientId == -1) {
                System.out.println("Client not found.");
            }
            return clientId;
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
            return -1; // Or handle the error as needed
        }
    }


    // Method to add a client
    public void addClient() throws SQLException {
        Main.DAO dao = new Main.DAO();
        dao.addClient(this);
    }

    // Method to get all clients
    public static List<Client> getAllClients() throws SQLException {
        Main.DAO dao = new Main.DAO();
        return dao.getAllClients();
    }
    // Method to convert Client to JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Method to create Client from JSON
    public static Client fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Client.class);
    }
}
