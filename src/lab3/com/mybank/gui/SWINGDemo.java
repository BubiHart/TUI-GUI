package lab3.com.mybank.gui;

import com.mybank.data.DataSource;
import com.mybank.domain.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author Divine Poseidon
 */
public class SWINGDemo {

    private final JEditorPane log;
    private final JButton show;
    private final JButton report;
    private final JComboBox clients;

    public SWINGDemo() {

        loadDataFromFile();
        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(1000, 850));
        show = new JButton("Show");
        report = new JButton("Show report");
        clients = new JComboBox();
        for (int counter = 0; counter < Bank.getNumberOfCustomers(); counter++) {
            clients.addItem(Bank.getCustomer(counter).getLastName() + ", " + Bank.getCustomer(counter).getFirstName());
        }

    }

    private void loadDataFromFile() {
        DataSource dataSource = new DataSource("data/test.dat");
        try {
            dataSource.loadData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Troubles with file loading");
        }

    }

    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());
        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 2));
        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);
        frame.add(cpane, BorderLayout.NORTH);
        frame.add(log, BorderLayout.CENTER);

        show.addActionListener(e -> {
            Customer current = Bank.getCustomer(clients.getSelectedIndex());
            StringBuilder custInfo = new StringBuilder();

            custInfo.append("<br>&nbsp;<b><span style=\"font-size:2em;\">").append(current.getLastName()).append(", ").append(current.getFirstName()).append("</span><br><hr>");

            for (int counter = 0; counter < current.getNumberOfAccounts(); counter++) {
                String accType = current.getAccount(counter) instanceof CheckingAccount ? "Checking" : "Savings";

                custInfo.append("&nbsp;<b>Acc Type: </b>").append(accType)
                        .append("<br>&nbsp;<b>Balance: <span style=\"color:red;\">$")
                        .append(current.getAccount(counter).getBalance())
                        .append("</span></b>")
                        .append("<br><br>");
            }

            log.setText(custInfo.toString());
        });

        report.addActionListener(e -> generateCustomerReport());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void generateCustomerReport() {
        StringBuilder report = new StringBuilder();
        report.append("<b>CUSTOMERS REPORT</b>")
                .append("<br>")
                .append("<span>================</span>")
                .append("<br>");

        for (int cust_idx = 0; cust_idx < Bank.getNumberOfCustomers(); ++cust_idx) {
            Customer customer = Bank.getCustomer(cust_idx);

            report.append("<span>Customer: <span>")
                    .append("<b>")
                    .append(customer.getLastName())
                    .append(", ")
                    .append(customer.getFirstName())
                    .append("</b>")
                    .append("<br>")
                    .append("<br>");

            for (int acct_idx = 0; acct_idx < customer.getNumberOfAccounts(); ++acct_idx) {
                Account account = customer.getAccount(acct_idx);
                String account_type = "";
                if (account instanceof SavingsAccount) {
                    account_type = "Savings Account";
                } else if (account instanceof CheckingAccount) {
                    account_type = "Checking Account";
                } else {
                    account_type = "Unknown Account Type";
                }

                report.append("    ")
                        .append(account_type)
                        .append(": current balance is ")
                        .append("<b>")
                        .append(account.getBalance())
                        .append("</b>")
                        .append("$")
                        .append("<br>");
            }

            report.append("<br>");
        }

        log.setText(report.toString());

    }

    public static void main(String[] args) {
        SWINGDemo demo = new SWINGDemo();
        demo.launchFrame();
    }

}
