package banking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Соддержит информацию об аккаунте
 * ________________________________
 * cardNumber, cardPIN, balance - соответсвующие данные
 */

public class Account {
    private String cardNumber;
    private String cardPIN;
    private long balance;

    public Account(String cardNumber, String cardPIN, long balance) {
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardPIN() {
        return cardPIN;
    }

    public long getBalance() {
        return balance;
    }

    public void addIncome(long income) {
        balance += income;
    }

    @Override
    public String toString() {
        return "Card Number: " + cardNumber + ", cardPIN: '" + cardPIN + ", balance=" + balance;
    }
}
