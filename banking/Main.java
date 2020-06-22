package banking;

import java.util.Scanner;

public class Main {
    public static boolean isExit = false;
    static Scanner scanner = new Scanner(System.in);
    static Bank jokeBank;
    static String fileName;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName")) {
                fileName = args[i + 1];
                break;
            }
        }

        jokeBank = new Bank(fileName);

        while (!isExit) {
            if (!jokeBank.isLogin()) {
                mainMenu();
            } else {
                loginAccountMenu();
            }
        }

        System.out.println("Bye!");
    }

    private static void mainMenu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");

        String command = scanner.next();

        switch (command) {
            case "1":
                jokeBank.createNewAccount();
                break;
            case "2":
                System.out.println("Enter your card number:");
                String cardNumber = scanner.next();
                System.out.println("Enter your PIN:");
                String cardPIN = scanner.next();
                boolean isSuccess = jokeBank.login(cardNumber, cardPIN);
                if (isSuccess) {
                    System.out.println("\nYou have successfully logged in!\n");
                } else {
                    System.out.println("\nWrong card number or PIN!\n");
                }
                break;
            case "0":
                isExit = true;
                break;
            default:
        }
    }

    private static void loginAccountMenu() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");

        String command = scanner.next();

        switch (command) {
            case "1":
                System.out.println("Balance: " + jokeBank.getBalance());
                break;
            case "2":
                System.out.println("Enter income:");
                jokeBank.addIncome(scanner.nextLong());
                System.out.println("Income was added!");
                break;
            case "3":
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                jokeBank.doTransfer(scanner.next());
                break;
            case "4":
                System.out.println("The account has been closed!");
                jokeBank.closeAnAccount();
                break;
            case "5":
                boolean isSuccess = jokeBank.logout();
                if (isSuccess) {
                    System.out.println("\nYou have successfully logged out!\n");
                } else {
                    System.out.println("\nWrong card number or PIN!\n");
                }
                break;
            case "0":
                isExit = true;
                break;
            default:
        }
    }
}
