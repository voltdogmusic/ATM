/*
ATM.java
Author Thomas Lee
4/9/2017
An ATM GUI that can perform withdrawals, transfers between a checking or savings account, deposits and can check the balance of either account.
 */
import javax.swing.*;
import java.awt.*;

/*
*****CONTAINS MAIN METHOD*****

This class contains the GUI code and has an Account object.
Interaction with the GUI will trigger an action listener.
The action listener methods will use the Account object to call upon the Account classes methods.

Program Flow:
1. Main creates an ATM object
2. The ATM object constructor call then calls
    a. the createGui method
    b. the setActionListeners method
3. Interaction with the GUI triggers the action listeners, which uses the Account object within the ATM class, to execute the relevant methods within the Account class.
 */
public class ATM extends JFrame {

    //ACCOUNT OBJECT, IMPORTANT, CONTAINS BUSINESS LOGIC
    private Account accountObj = new Account();

    private static JFrame frame = new JFrame();
    private JPanel panel = new JPanel(new GridBagLayout());

    private JButton withdrawButton = new JButton("Withdraw");
    private JButton depositButton = new JButton("Deposit");
    private JButton transferToButton = new JButton("Transfer to");
    private JButton balanceButton = new JButton("Balance");

    private ButtonGroup buttonGroup = new ButtonGroup();
    private static JRadioButton checkingRadioButton = new JRadioButton("Checking");
    private static JRadioButton savingsRadioButton = new JRadioButton("Savings");

    private static JTextField textField = new JTextField();
    //VARIABLE FOR THE TEXT FIELD INPUT
    private static double moneyInputVar;


    //GETTERS FOR ACCOUNT CLASS ACCESS
    public static JRadioButton getCheckingRadioButton() {
        return checkingRadioButton;
    }
    public static JRadioButton getSavingsRadioButton() {
        return savingsRadioButton;
    }
    public static JTextField getTextField() {
        return textField;
    }
    public static double getMoneyInputVar() {
        return moneyInputVar;
    }

    //CONSTRUCTOR, CONTAINS CALLS TO THIS CLASSES METHODS BELOW
    private ATM() {
        createGui();
        setActionListeners();
    }

    private void createGui() {

        frame.setSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("ATM Machine");
        frame.setResizable(true);
        frame.setVisible(true);

        panel.setBackground(Color.lightGray);
        frame.getContentPane().add(panel);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;
        panel.add(withdrawButton, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(depositButton, c);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(transferToButton, c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(balanceButton, c);
        c.gridx = 0;
        c.gridy = -2;
        panel.add(checkingRadioButton, c);
        c.gridx = 1;
        c.gridy = -2;
        panel.add(savingsRadioButton, c);
        c.gridx = 0;
        c.gridwidth = 10;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = .5;
        c.weighty = .5;
        c.insets = new Insets(10, 10, 10, 10);
        textField.setPreferredSize(new Dimension(200, 10));
        panel.add(textField, c);


        //ensures only one button can be selected at once
        buttonGroup.add(checkingRadioButton);
        buttonGroup.add(savingsRadioButton);
    }

    private void setActionListeners() {

        withdrawButton.addActionListener(a -> {

            accountObj.withdrawMethod();

        });

        depositButton.addActionListener(a -> {

            accountObj.depositMethod();

        });

        transferToButton.addActionListener(a -> {

            accountObj.transferToMethod();

        });

        balanceButton.addActionListener(a -> {

            accountObj.balanceMethod();

        });

    }

    //MAIN METHOD*******************************
    public static void main(String[] args) {

        //this constructor call will call the createGui and setActionListeners method
        ATM ATMClassObj = new ATM();

    }
}

class Account {

    //SET TO 100 LATER AFTER TESTING
    private static double checkingAmount = 100;
    private static double savingsAmount = 100;
    private int withdrawCount;  //# of withdrawals, used to calculate withdrawal penalty
    private double moneyInputVar = ATM.getMoneyInputVar();
    private static JTextField textField = ATM.getTextField();

    public Account() {
    ATM.getCheckingRadioButton().setSelected(true);
    }

    private boolean  checkIfValidInput(){

        try {//ensure user is entering a numeric value
            moneyInputVar = Double.parseDouble(textField.getText());
            if (moneyInputVar < 0) {
                JOptionPane.showMessageDialog(textField, "Please enter a positive value", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }//end try
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(textField, "Please enter a numeric value", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }//end catch

        return true;
    }

    public void withdrawMethod() {

       if(!checkIfValidInput()){
           return;
       }

        if (ATM.getCheckingRadioButton().isSelected()) {
            try {
                if (moneyInputVar > checkingAmount) {
                    throw new InsufficientFunds();
                }
            }
            catch (InsufficientFunds x) {
                JOptionPane.showMessageDialog(textField, "You do not have enough money to do that", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(textField, "You have made a wtihdraw", "Thanks", JOptionPane.PLAIN_MESSAGE);
            checkingAmount = checkingAmount - moneyInputVar;

        } else if (ATM.getSavingsRadioButton().isSelected()) {
            try {
                if (moneyInputVar > savingsAmount) {
                    throw new InsufficientFunds();
                }
            }
            catch (InsufficientFunds x) {
                JOptionPane.showMessageDialog(textField, "You do not have enough money to do that", "Error", JOptionPane.ERROR_MESSAGE);
                return;

            }
            JOptionPane.showMessageDialog(textField, "You have made a wtihdraw", "Thanks", JOptionPane.PLAIN_MESSAGE);
            savingsAmount = savingsAmount - moneyInputVar;

        } else {
            JOptionPane.showMessageDialog(textField, "Please Select Checking or Savings", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //LOGIC TO DEDUCT 1.50 FROM ACCOUNT IF >= 4 WITHDRAWS ARE MADE
        withdrawCount++;

        if (withdrawCount >= 3) {
            JOptionPane.showMessageDialog(null, "You will be charged 1.50 on your next withdrawal");
        }

        if (withdrawCount >= 4) {
            if (ATM.getCheckingRadioButton().isSelected()) {

                try {
                    if (1.50 > checkingAmount) {
                        throw new InsufficientFunds();
                    }
                }
                catch (InsufficientFunds x) {
                    JOptionPane.showMessageDialog(textField, "You do not have enough money to do that", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                checkingAmount = checkingAmount - (1.50);

            } else {
                try {
                    if (1.50 > savingsAmount) {
                        throw new InsufficientFunds();
                    }
                }
                catch (InsufficientFunds x) {
                    JOptionPane.showMessageDialog(textField, "You do not have enough money to do that", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                savingsAmount = savingsAmount - (1.50);
            }
            JOptionPane.showMessageDialog(null, "Your have been charged 1.50$ for making more than 4 withdrawals");
        }
    }

    public void transferToMethod() {
        if(!checkIfValidInput()){
            return;
        }
        if (ATM.getCheckingRadioButton().isSelected()) {
            int result = JOptionPane.showConfirmDialog(null,
                    "You are transferring funds from your savings account into your checking account, is this ok?",null, JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION) {
            try {
                if (savingsAmount < moneyInputVar) {
                    throw new InsufficientFunds();
                }
            }
            catch (InsufficientFunds x) {
                JOptionPane.showMessageDialog(textField, "You do not have enough money to do that", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(textField, "You have transferred funds into your checking account from your savings account", "Thanks", JOptionPane.PLAIN_MESSAGE);
            //logic to transfer funds from account to account
            checkingAmount = checkingAmount + moneyInputVar;
            savingsAmount = savingsAmount - moneyInputVar;
            }else{//else if no is selected exit this method
                return;
            }
        }
        else if (ATM.getSavingsRadioButton().isSelected()) {
            int result = JOptionPane.showConfirmDialog(null,
                    "You are transferring funds from your checking account into your savings account, is this ok?",null, JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION) {
            try {
                if (checkingAmount < moneyInputVar) {
                    throw new InsufficientFunds();

                }
            }
            catch (InsufficientFunds x) {
                JOptionPane.showMessageDialog(textField, "You do not have enough money to do that", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(textField, "You have transferred funds into your savings account from your checking account", "Thanks", JOptionPane.PLAIN_MESSAGE);
            //logic to transfer funds from account to account
            checkingAmount = checkingAmount - moneyInputVar;
            savingsAmount = savingsAmount + moneyInputVar;
            }else{//else if no is selected exit this method
                return;
            }
        }
        else {
            JOptionPane.showMessageDialog(textField, "Please Select Checking or Savings", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void depositMethod() {

        if(!checkIfValidInput()){
            return;
        }

        while (!(moneyInputVar % 20 == 0)) {
            JOptionPane.showMessageDialog(textField, "Please enter an increment of 20$ ", "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        if (ATM.getSavingsRadioButton().isSelected()) {

            checkingAmount = checkingAmount + moneyInputVar;
            JOptionPane.showMessageDialog(textField, "You have made a deposit", "Thanks", JOptionPane.PLAIN_MESSAGE);

        } else if (ATM.getCheckingRadioButton().isSelected()) {
            savingsAmount = savingsAmount + moneyInputVar;
            JOptionPane.showMessageDialog(textField, "You have made a deposit", "Thanks", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(textField, "Please Select Checking or Savings", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void balanceMethod() {
        if (ATM.getCheckingRadioButton().isSelected()) {
            JOptionPane.showMessageDialog(null, "Your checking account has  " + checkingAmount);
        } else if (ATM.getSavingsRadioButton().isSelected()) {
            JOptionPane.showMessageDialog(null, "Your savings account has  " + savingsAmount);
        } else {
            JOptionPane.showMessageDialog(textField, "Please Select Checking or Savings", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}


class InsufficientFunds extends Exception {

    public InsufficientFunds() {

    }//end exception class constructor

}

