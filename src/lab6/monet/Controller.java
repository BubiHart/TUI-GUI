package lab6.monet;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;

/**
 * @author Divine Poseidon
 */
public class Controller implements Initializable {

    @FXML
    public TextArea textArea;
    public ComboBox comboBox;

    @FXML
    private void handleButtonActionShow(ActionEvent event) {
        // Button was clicked, do something…
        int custNo = comboBox.getItems().indexOf(comboBox.getValue()) + 1;
        textArea.setText(Bank.getCustomer(custNo - 1).getLastName() + " " + Bank.getCustomer(custNo - 1).getFirstName() + ", customer #" + custNo + "\n-------------------------\n");

        String accType = Bank.getCustomer(custNo - 1).getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";

        if (Bank.getCustomer(custNo - 1).getNumberOfAccounts() == 2) {
            String accType1 = Bank.getCustomer(custNo - 1).getAccount(1) instanceof CheckingAccount ? "Checking" : "Savings";
            textArea.appendText("Accounts:\t\t#" + 2 + "\n#1 - \t" + accType +
                    ":\t\t$" + Bank.getCustomer(custNo - 1).getAccount(0).getBalance() +
                    "\n#2 - \t" + accType1 + ":\t\t$" + Bank.getCustomer(custNo - 1).getAccount(1).getBalance());
        } else {
            textArea.appendText("Account:\t\t#" + 1 + "\n#1 - \t" + accType + ":\t\t$" + Bank.getCustomer(custNo - 1).getAccount(0).getBalance());
        }

    }

    @FXML
    private void handleButtonActionReport(ActionEvent event) throws Exception {
        StringBuilder reportInfo = new StringBuilder("\t\tCUSTOMER REPORT\n");
        for (int cust_idx = 0;
             cust_idx < Bank.getNumberOfCustomers();
             cust_idx++) {
            Customer customer = Bank.getCustomer(cust_idx);
            // Print the customer's name
            reportInfo.append("\nCustomer: ").append(customer.getLastName()).append(", ").append(customer.getFirstName());
            // For each account for this customer...
            for (int acct_idx = 0;
                 acct_idx < customer.getNumberOfAccounts();
                 acct_idx++) {
                Account account = customer.getAccount(acct_idx);
                String account_type = "";
                // Determine the account type
                if (account instanceof SavingsAccount) {
                    account_type = "Savings Account";
                } else if (account instanceof CheckingAccount) {
                    account_type = "Checking Account";
                } else {
                    account_type = "Unknown Account Type";
                }
                // Print the current balance of the account
                reportInfo.append("\n    ").append(account_type).append(": current balance is ").append(account.getBalance()).append("$");
            }
            reportInfo.append("\n");
        }
        textArea.setText(reportInfo.toString());
    }

    @FXML
    private void handleButtonActionAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Just a simple Monet demo.\nCopyright \u00A9 Divine Poseidon");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (int counter = 0; counter < Bank.getNumberOfCustomers(); counter++) {
            list.add(Bank.getCustomer(counter).getLastName() + " " + Bank.getCustomer(counter).getFirstName());
        }
        comboBox.setItems(list);
        comboBox.setPromptText("Click to choose...");
    }
}
