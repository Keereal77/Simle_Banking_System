package banking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Класс представляет банк
 * _______________________
 * currentAccount - аккаунт для которого выполнен вход, в противном случае null
 * isLogin - указывает выполнен ли вход вообще
 * handler - хранит экземпляр класса DatabaseHandler
 * _______________________
 * createNewAccount() - создаёт новый аккаунт и добавляет его в базу данных
 * login() - осушествляет вход и возвращет true, если вход успешен (совпали номер карты и ПИН),
 *           или false, если не совпал ПИН или такой карты не существует
 * logout() - осушествляет выход и возвращает true, если выход успешен, или false, если вход не происходил
 * getBalance() - возвращает баланс текущего аккаунта или -1, если вход не выполнен
 */

public class Bank {
    Account currentAccount;
    boolean isLogin;
    DatabaseHandler handler;

    public Bank(String filename) {
        isLogin = false;
        handler = DatabaseHandler.getInstance(filename);
    }

    public void createNewAccount() {
        String cardNumber = CardGenerator.createNewCardNumber();
        String cardPIN = CardGenerator.createNewPIN();

        System.out.println("\nYour card has been created");
        System.out.println("Your card number:\n" + cardNumber);
        System.out.println("Your card PIN:\n" + cardPIN + "\n");
        handler.addCard(new Account(cardNumber, cardPIN, 0));
    }

    public boolean login(String cardNumber, String cardPIN) {
        Account account = handler.getAccountInfo(cardNumber, cardPIN);
        if (account != null) {
            currentAccount = account;
            isLogin = true;
            return true;
        }
        return false;
    }

    public boolean logout() {
        if (isLogin) {
            isLogin = false;
            currentAccount = null;
            return true;
        } else {
            return false;
        }
    }

    public long getBalance() {
        if (isLogin) {
            return currentAccount.getBalance();
        } else {
            return -1;
        }
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void addIncome(long income) {
        if (currentAccount != null) {
            currentAccount.addIncome(income);
        }
    }

    public void doTransfer(String recipientCardNumber) {
        if (recipientCardNumber.equals(currentAccount.getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }

        if (!luhnVerification(recipientCardNumber)) {
            System.out.println("Probably you made mistake in card number. Please try again!");
            return;
        }

        ResultSet rs = handler.selectWhere("number", "number = " + recipientCardNumber);
        if (rs == null) {
            System.out.println("Such a card does not exist.");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        Scanner scanner = new Scanner(System.in);
        long transferValue = scanner.nextLong();
        scanner.close();

        if (transferValue > currentAccount.getBalance()) {
            System.out.println("Not enough money!");
            return;
        }

        boolean transferRes = handler.updateWhere("balance = balance + " + transferValue,
                "number = " + recipientCardNumber);

        if (transferRes) {
            System.out.println("Success!");
        }
    }

    private boolean luhnVerification(String recipientCardNumber) {
        int sum = 0;

        for (int i = 0; i < recipientCardNumber.length() - 1; i += 2) {
            int digit = Character.getNumericValue(recipientCardNumber.charAt(i));
            digit *= 2;
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }

        for (int i = 1; i < recipientCardNumber.length() - 1; i += 2) {
            int digit = Character.getNumericValue(recipientCardNumber.charAt(i));
            sum += digit;
        }

        int checkSum = 10 - (sum % 10);
        if (checkSum == 10) {
            checkSum = 0;
        }

        return checkSum == recipientCardNumber.charAt(recipientCardNumber.length() - 1);
    }

    public void closeAnAccount() {
        handler.deleteWhere("number = " + currentAccount.getCardNumber());
        currentAccount = null;
        isLogin = false;
    }
}
