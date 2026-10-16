package com.safepay;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Starts the transaction import and prints the results.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("SafePay: importing transaction data");
        System.out.println();

        Path csvPath = Path.of("data", "transactions.csv");
        CsvTransactionReader reader = new CsvTransactionReader();

        List<Transaction> transactions;
        try {
            transactions = reader.read(csvPath);
        } catch (IOException exception) {
            System.out.println("Could not read " + csvPath + ": " + exception.getMessage());
            return;
        }

        List<String> errors = reader.getErrors();
        for (String error : errors) {
            System.out.println(error);
        }
        if (!errors.isEmpty()) {
            System.out.println();
        }

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println();
        System.out.println("Successfully parsed transactions: " + transactions.size());
        System.out.println("Rows that failed parsing: " + errors.size());
    }
}
