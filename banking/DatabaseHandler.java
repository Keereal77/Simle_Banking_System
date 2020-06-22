package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для работы с базой данных
 * _______________________________
 * Используется синглтон. При создании экземпляра необходимо указать название файла с базой данных для установления
 * связи и создания таблицы card (в конструкторе), иначе вернёт null(костыль), в дальнейшем для получения существующего
 * экземпляра название можно не указывать.
 * _______________________________
 * getInstance() - получение экземпляра
 * addCard(Account account) - добавление новой записи в базу данных
 * getAccountInfo(String number, String pin) - возвращает аккаунт с соотв. данными или null, если такого аккаунта нет
 * getInfoAbout(String option) - делает запрос и возвращае указанные столбцы из базы данных
 */

public class DatabaseHandler {
    private static DatabaseHandler handlerInstance;
    private static SQLiteDataSource dataSource = new SQLiteDataSource();

    private DatabaseHandler(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            String query = "CREATE TABLE card (" +
                    "id INTEGER, " +
                    "number TEXT, " +
                    "pin TEXT, " +
                    "balance INTEGER DEFAULT 0)";
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseHandler getInstance(String fileName) {
        if (handlerInstance == null) {
            handlerInstance = new DatabaseHandler(fileName);
        }
        return handlerInstance;
    }

    public static DatabaseHandler getInstance() {
        return handlerInstance;
    }

    public void addCard(Account account) {
        String number = account.getCardNumber();
        String pin = account.getCardPIN();

        try (Connection con = dataSource.getConnection()) {
            String query1 = "SELECT max(id) FROM card";
            Statement statement1 = con.createStatement();
            ResultSet rs = statement1.executeQuery(query1);
            int id = 0;


            if (rs != null) {
                id = rs.getInt("max(id)") + 1;
            }

            String query2 = "INSERT INTO card (id, number, pin, balance) " +
                    "VALUES (" + id + ", '" + number + "', '" + pin + "', 0)";
            Statement statement2 = con.createStatement();
            statement2.executeUpdate(query2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccountInfo(String number, String pin) {
        Account result = null;

        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT number, pin, balance FROM card WHERE number = " + number + " AND pin = " + pin;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if (rs != null) {
                long balance = rs.getInt("balance");
                result = new Account(number, pin, balance);
                System.out.println(rs.getString("pin"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ResultSet select(String option) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT " + option + " FROM card";
            Statement statement = con.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectWhere(String select, String where) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT " + select + " FROM card WHERE " + where;
            Statement statement = con.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateWhere(String set, String where) {
        try (Connection con = dataSource.getConnection()) {
            String query = "UPDATE card SET " + set + " WHERE " + where;
            Statement statement = con.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteWhere(String where) {
        try (Connection con = dataSource.getConnection()) {
            String query = "DELETE FROM card WHERE " + where;
            Statement statement = con.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
