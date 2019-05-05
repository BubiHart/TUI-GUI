package lab5.com.mybank.gui;

import com.mybank.data.DataSource;
import com.mybank.domain.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Divine Poseidon
 */
public class FXDemo extends Application {

    private Text title;
    private Text details;
    private ComboBox clients;

    @Override
    public void start(Stage primaryStage) {
        retrieveDataFromFile();
        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        addStackPane(hbox);

        Scene scene = new Scene(border, 450, 350);

        primaryStage.setTitle("MyBank Clients");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void retrieveDataFromFile() {
        DataSource dataSource = new DataSource("data/test.dat");
        try {
            dataSource.loadData();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No file, data loading failed");
            alert.showAndWait();
        }
    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        title = new Text("Client Name");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        vbox.getChildren().add(title);

        Line separator = new Line(10, 10, 280, 10);
        vbox.getChildren().add(separator);

        details = new Text("Account:\t\t#0\nAccount Type:\tChecking\nBalance:\t\t$0000");
        details.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        vbox.getChildren().add(details);

        return vbox;
    }


    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            items.add(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }

        clients = new ComboBox(items);
        clients.setPrefSize(150, 20);
        clients.setPromptText("Click to choose...");

        Button buttonShow = new Button("Show");
        buttonShow.setPrefSize(100, 20);

        buttonShow.setOnAction(event -> {
            try {
                int custNo = clients.getItems().indexOf(clients.getValue());
                int accNo = 0;
                title.setText(clients.getValue().toString());
                String accType = Bank.getCustomer(custNo).getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";

                if (Bank.getCustomer(custNo).getNumberOfAccounts() == 2) {
                    String accountType = Bank.getCustomer(custNo).getAccount(1) instanceof CheckingAccount ? "Checking" : "Savings";
                    details.setText("Accounts:\t\t#" + 2 + "\n\nAcc Type:\t\t" + accType +
                            "\nBalance:\t\t$" + Bank.getCustomer(custNo).getAccount(0).getBalance() +
                            "\n\nAcc Type:\t\t" + accountType + "\nBalance:\t\t$" + Bank.getCustomer(custNo).getAccount(1).getBalance());
                } else {
                    details.setText("Account:\t\t#" + 1 + "\n\nAcc Type:\t\t" + accType + "\nBalance:\t\t$" + Bank.getCustomer(custNo).getAccount(0).getBalance());
                }
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error getting client...");
                // Header Text: null
                alert.setHeaderText(null);
                String details = e.getMessage() != null ? e.getMessage() : "You need to choose a client first!";
                alert.setContentText("Error details: " + details);
                alert.showAndWait();
            }
        });

        Button buttonReport = new Button("Report");
        buttonReport.setPrefSize(100, 20);

        buttonReport.setOnAction(event -> {
            StringBuilder reportInfo = new StringBuilder("\n");
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
            Text title;
            Text report;
            StackPane secondaryLayout = new StackPane();
            title = new Text("CUSTOMERS REPORT");
            title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

            report = new Text(reportInfo.toString());
            report.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

            secondaryLayout.setAlignment(Pos.TOP_LEFT);
            secondaryLayout.setPadding(new Insets(15, 12, 15, 12));

            secondaryLayout.getChildren().addAll(title, report);
            Scene secondScene = new Scene(secondaryLayout, 400, 280);

            Stage newWindow = new Stage();
            newWindow.setTitle("Customers Report");
            newWindow.setScene(secondScene);
            newWindow.show();
        });


        hbox.getChildren().addAll(clients, buttonShow, buttonReport);

        return hbox;
    }

    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#4977A3")),
                new Stop(0.5, Color.web("#B0C6DA")),
                new Stop(1, Color.web("#9CB6CF"))));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        helpIcon.setOnMouseClicked(t -> ShowAboutInfo());

        helpText.setOnMouseClicked(t -> ShowAboutInfo());

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0));

        hb.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
    }

    private void ShowAboutInfo() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Just a simple JavaFX demo.\n \u00A9 Divine Poseidon");
        alert.showAndWait();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}