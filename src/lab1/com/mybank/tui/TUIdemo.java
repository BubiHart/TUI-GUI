package lab1.com.mybank.tui;

import com.mybank.domain.OverDraftAmountException;
import jexer.*;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.Account;
import com.mybank.data.DataSource;

/**
 * @author Divine Poseidon
 */
public class TUIdemo extends TApplication {

    private static final int ABOUT_APP = 2000;
    private static final int CUST_INFO = 2010;
    private static final int ADD_CUST = 2020;

    public static void main(String[] args) throws Exception {
        TUIdemo tdemo = new TUIdemo();
        (new Thread(tdemo)).start();
    }

    public TUIdemo() throws Exception {
        super(BackendType.SWING);

        addToolMenu();

        DataSource DataSource = new DataSource("data/test.dat");
        DataSource.loadData();

        //custom 'File' menu
        TMenu fileMenu = addMenu("&File");
        fileMenu.addItem(CUST_INFO, "&Customer Info");
        fileMenu.addItem(ADD_CUST, "&Add customer");
        fileMenu.addDefaultItem(TMenu.MID_SHELL);
        fileMenu.addSeparator();
        fileMenu.addDefaultItem(TMenu.MID_EXIT);
        //end of 'File' menu

        addWindowMenu();

        //custom 'Help' menu
        TMenu helpMenu = addMenu("&Help");
        helpMenu.addItem(ABOUT_APP, "&About...");
        //end of 'Help' menu
        ShowCustomerDetails();
        setFocusFollowsMouse(true);

    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {

        switch (menu.getId()) {
            case ABOUT_APP:
                messageBox("About", "\t\t\t\t\t   Just a simple Jexer demo.\n\nCopyright \u00A9 Divine Poseidon").show();
                return true;

            case CUST_INFO:
                ShowCustomerDetails();
                return true;

            case ADD_CUST:
                AddCustomer();
                return true;
            default:
                return super.onMenu(menu);
        }

    }

    /**
     * @author DivinePoseidon
     */
    private void AddCustomer() {
        TWindow customWindow = addWindow("Add customer", 2, 2, 60, 15, TWindow.NOZOOMBOX);
        customWindow.newStatusBar("Enter valid customer data and press Enter");

        customWindow.addLabel("Enter customer first name: ", 2, 2);
        TField customerFirstNameInput = customWindow.addField(28, 2, 12, false);
        customWindow.addLabel("Enter customer last name: ", 2, 4);
        TField customerLastNameInput = customWindow.addField(27, 4, 12, false);
        customWindow.addLabel("Enter customer balance: ", 2, 6);
        TField customerBalanceInput = customWindow.addField(25, 6, 10, false);

        customWindow.addButton("&Add", 25, 8, new TAction() {
            @Override
            public void DO() {
                try {
                    String customerFirstName = customerFirstNameInput.getText();
                    String customerLastName = customerLastNameInput.getText();
                    double customerBalance = Double.parseDouble(customerBalanceInput.getText());

                    Bank.addCustomer(customerFirstName, customerLastName);
                    int newCustomerIndex = Bank.getNumberOfCustomers() - 1;
                    Customer customer = Bank.getCustomer(newCustomerIndex);

                    Account customerAccount = new Account(customerBalance) {
                        @Override
                        public boolean withdraw(double v) throws OverDraftAmountException {
                            return false;
                        }
                    };

                    customer.addAccount(customerAccount);

                    TMessageBox successMessageBox = messageBox("Success", "New customer has been added\nNew account number is " + newCustomerIndex + "\n");
                    successMessageBox.show();
                    customerFirstNameInput.setText("");
                    customerLastNameInput.setText("");
                    customerBalanceInput.setText("");
                } catch (Exception e) {
                    messageBox("Error", "You must provide a valid data!").show();
                }
            }
        });
    }


    private void ShowCustomerDetails() {
        TWindow custWin = addWindow("Customer Window", 2, 1, 40, 10, TWindow.NOZOOMBOX);
        custWin.newStatusBar("Enter valid customer number and press Show...");

        custWin.addLabel("Enter customer number: ", 2, 2);
        TField custNo = custWin.addField(24, 2, 3, false);
        TText details = custWin.addText("Owner Name: \nAccount Balance: ", 2, 4, 38, 8);
        custWin.addButton("&Show", 28, 2, new TAction() {
            @Override
            public void DO() {
                try {
                    int custNum = Integer.parseInt(custNo.getText());
                    Customer customer = Bank.getCustomer(custNum);
                    Account customerAccount = customer.getAccount(0);
                    details.setText("Owner Name:" + customer.getFirstName() + " " + customer.getLastName() + "(id=" + custNum + ")\nAccount Balance: " + customerAccount.getBalance() + "$");
                } catch (Exception e) {
                    messageBox("Error", "You must provide a valid customer number!").show();
                }
            }
        });
    }
}
