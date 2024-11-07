package java.org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MyApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Bank Management System");

        // Create a TabPane for navigation
        TabPane tabPane = new TabPane();

        // Create tabs for each functionality
        Tab clientTab = new Tab("Clients");
        Tab bankTab = new Tab("Banks");
        Tab accountTab = new Tab("Accounts");
        Tab transactionTab = new Tab("Transactions");
        Tab searchTab = new Tab("Search"); // New Search Tab

        // Set the content for each tab
        clientTab.setContent(createClientTab());
        bankTab.setContent(createBankTab());
        accountTab.setContent(createAccountTab());
        transactionTab.setContent(createTransactionTab());
        searchTab.setContent(createSearchTab()); // Set search tab content

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(clientTab, bankTab, accountTab, transactionTab, searchTab);

        // Create the main scene and add the TabPane
        Scene scene = new Scene(tabPane, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private VBox createSearchTab() throws SQLException {
        Main.DAO dao = new Main.DAO();
        VBox vbox = new VBox(10); // Vertical layout with spacing of 10

        TextField searchClientField = new TextField();
        searchClientField.setPromptText("Search Client by ID or Name");

        Button searchClientButton = new Button("Search Client");
        ListView<String> clientResults = new ListView<>(); // Display client results

        searchClientButton.setOnAction(e -> {
            String query = searchClientField.getText();
            try {
                List<Client> clients = dao.searchClients(query);
                clientResults.getItems().clear();
                for (Client client : clients) {
                    clientResults.getItems().add(client.toJson()); // Display client info as JSON (or customize as needed)
                }
            } catch (SQLException ex) {
                showErrorDialog("Error searching for clients: " + ex.getMessage());
            }
        });

        TextField searchAccountField = new TextField();
        searchAccountField.setPromptText("Search Account by ID or Type");

        Button searchAccountButton = new Button("Search Account");
        ListView<String> accountResults = new ListView<>(); // Display account results

        searchAccountButton.setOnAction(e -> {
            String query = searchAccountField.getText();
            try {
                List<Account> accounts = dao.searchAccounts(query);
                accountResults.getItems().clear();
                for (Account account : accounts) {
                    accountResults.getItems().add(account.toJson()); // Display account info as JSON (or customize as needed)
                }
            } catch (SQLException ex) {
                showErrorDialog("Error searching for accounts: " + ex.getMessage());
            }
        });

        vbox.getChildren().addAll(searchClientField, searchClientButton, clientResults, searchAccountField, searchAccountButton, accountResults);
        return vbox;
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    private VBox createClientTab() {
        VBox vbox = new VBox(10); // Vertical layout with spacing of 10

        TextField clientFirstName = new TextField();
        clientFirstName.setPromptText("First Name");
        TextField clientLastName = new TextField();
        clientLastName.setPromptText("Last Name");
        TextField clientEmail = new TextField();
        clientEmail.setPromptText("Email");
        TextField clientPhone = new TextField();
        clientPhone.setPromptText("Phone");
        TextField clientAddress = new TextField();
        clientAddress.setPromptText("Address");

        Button addClientButton = new Button("Add Client");
        addClientButton.setOnAction(e -> addClient(clientFirstName.getText(), clientLastName.getText(), clientEmail.getText(), clientPhone.getText(), clientAddress.getText()));

        vbox.getChildren().addAll(clientFirstName, clientLastName, clientEmail, clientPhone, clientAddress, addClientButton);
        return vbox;
    }

    private VBox createBankTab() {
        VBox vbox = new VBox(10); // Vertical layout with spacing of 10

        TextField bankName = new TextField();
        bankName.setPromptText("Bank Name");
        TextField bankCountry = new TextField();
        bankCountry.setPromptText("Country");

        Button addBankButton = new Button("Add Bank");
        addBankButton.setOnAction(e -> addBank(bankName.getText(), bankCountry.getText()));

        vbox.getChildren().addAll(bankName, bankCountry, addBankButton);
        return vbox;
    }

    private VBox createAccountTab() {
        VBox vbox = new VBox(10); // Vertical layout with spacing of 10

        TextField accountType = new TextField();
        accountType.setPromptText("Account Type");
        TextField clientIdField = new TextField();
        clientIdField.setPromptText("Client ID");
        TextField bankIdField = new TextField();
        bankIdField.setPromptText("Bank ID");

        Button addAccountButton = new Button("Add Account");
        addAccountButton.setOnAction(e -> addAccount(accountType.getText(), Integer.parseInt(clientIdField.getText()), Integer.parseInt(bankIdField.getText())));

        vbox.getChildren().addAll(accountType, clientIdField, bankIdField, addAccountButton);
        return vbox;
    }

    private VBox createTransactionTab() {
        VBox vbox = new VBox(10); // Vertical layout with spacing of 10

        TextField transactionAmount = new TextField();
        transactionAmount.setPromptText("Amount");
        TextField transactionDescription = new TextField();
        transactionDescription.setPromptText("Description");
        TextField accountDebitField = new TextField();
        accountDebitField.setPromptText("Debit Account ID");
        TextField accountCreditField = new TextField();
        accountCreditField.setPromptText("Credit Account ID");

        Button addTransactionButton = new Button("Add Transaction");
        addTransactionButton.setOnAction(e -> addTransaction( Double.parseDouble(transactionAmount.getText()), transactionDescription.getText(), Integer.parseInt(accountDebitField.getText()), Integer.parseInt(accountCreditField.getText())));

        vbox.getChildren().addAll(transactionAmount, transactionDescription, accountDebitField, accountCreditField, addTransactionButton);
        return vbox;
    }

    private void addClient(String firstName, String lastName, String email, String phone, String address) {
        try {
            Client client = new Client();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setEmail(email);
            client.setPhone(phone);
            client.setAddress(address);
            client.addClient();
            System.out.println("Client added: " + client.toJson());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBank(String bankName, String country) {
        try {
            Bank bank = new Bank();
            bank.setBankName(bankName);
            bank.setCountry(country);
            bank.addBank();
            System.out.println("Bank added: " + bank.toJson());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAccount(String accountType, int clientId, int bankId) {
        try {
            Account account = new Account();
            account.setDate_creation(new Date(System.currentTimeMillis()));
            account.setDate_update(new Date(System.currentTimeMillis()));
            account.setAccount_type(accountType);
            // Assuming you have methods to find a client and bank by ID
            Main.DAO dao = new Main.DAO();
            account.setClient(dao.getClientById(clientId));
            account.setBank(dao.getBankById(bankId));
            account.addAccount();
            System.out.println("Account added: " + account.toJson());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTransaction( double amount, String description, int debitAccountId, int creditAccountId) {
        try {
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDate_transaction(new Date(System.currentTimeMillis()));
            transaction.setDescription(description);
            Main.DAO dao = new Main.DAO();
            transaction.setAccount_debit(dao.getAccountById(debitAccountId)); // Fetch debit account
            transaction.setAccount_credit(dao.getAccountById(creditAccountId)); // Fetch credit account
            transaction.addTransaction();
            System.out.println("Transaction added: " + transaction.toJson());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
