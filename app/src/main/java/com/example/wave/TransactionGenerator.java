package com.example.wave;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TransactionGenerator {

    private static final String[] NAMES = {"Aurlin", "Franck", "Dieng", "David", "Emma", "Sosthene"};
    private static final String[] PHONE_NUMBERS = {"788545440", "771445440", "78545440", "771445440", "788111920", "784758440"};
    private static final boolean[] TRANSACTIONS = {true, false};
    private static final float MIN_AMOUNT = 500f;
    private static final float MAX_AMOUNT = 10000f;

    public static List<Transaction> generateTransactions(int count) {
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Transaction transaction = new Transaction();
            transaction.setSenderName(getRandomName(random));
            transaction.setSenderPhoneNumber(getRandomPhoneNumber(random));
            transaction.setAmount(getRandomAmount(random));
            transaction.setTransaction(getRandomTransaction(random));
            transaction.setDate(new Date()); // You can customize date generation

            transactions.add(transaction);
        }

        return transactions;
    }

    private static String getRandomName(Random random) {
        return NAMES[random.nextInt(NAMES.length)];
    }

    private static String getRandomPhoneNumber(Random random) {
        return PHONE_NUMBERS[random.nextInt(PHONE_NUMBERS.length)];
    }

    private static float getRandomAmount(Random random) {
        return MIN_AMOUNT + random.nextFloat() * (MAX_AMOUNT - MIN_AMOUNT);
    }

    private static boolean getRandomTransaction(Random random) {
        return TRANSACTIONS[random.nextInt(TRANSACTIONS.length)];
    }
}