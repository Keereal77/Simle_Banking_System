package banking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Класс для генерации данных для новых карт
 * _________________________________________
 * createNewCardNumber(), createNewPIN() - генерирует номер и ПИН соответственно
 * generateCheckSum(String number) - вспомогательный метод для вычисления последней цифры в номере,
 *                                   вызывается из createNewCardNumber()
 * checkUnique(String cardNumber) - вспомогательный метод для проверки на уникальность,
 *                                  вызывается в конце createNewCardNumber() и в противном случае снова вызывает
 *                                  createNewCardNumber()
 */

public class CardGenerator {

    public static String createNewCardNumber() {
        Random random = new Random();
        StringBuilder newCardNum = new StringBuilder("400000");

        for (int i = 0; i < 9; i++) {
            newCardNum.append(random.nextInt(10));
        }
        newCardNum.append(generateCheckSum(newCardNum.toString()));
        return checkUnique(newCardNum.toString());
    }

    private static int generateCheckSum(String number) {
        int sum = 0;

        for (int i = 0; i < number.length(); i += 2) {
            int digit = Character.getNumericValue(number.charAt(i));
            digit *= 2;
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }

        for (int i = 1; i < number.length(); i += 2) {
            int digit = Character.getNumericValue(number.charAt(i));
            sum += digit;
        }

        int checkSum = 10 - (sum % 10);
        if (checkSum == 10) {
            checkSum = 0;
        }

        return checkSum;
    }

    private static String checkUnique(String cardNumber) {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        ResultSet rs = handler.select("number");
        try {
            while (rs.next()) {
                String number = rs.getString("number");
                if (cardNumber.equals(number)) {
                    return createNewCardNumber();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardNumber;
    }

    public static String createNewPIN() {
        Random random = new Random();
        StringBuilder newPIN = new StringBuilder(String.valueOf(random.nextInt(10000)));

        while (newPIN.length() < 4) {
            newPIN.insert(0, 0);
        }

        return newPIN.toString();
    }

}
