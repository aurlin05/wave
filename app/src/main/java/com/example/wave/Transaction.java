package com.example.wave;

import java.util.Date;

public class Transaction {
    private String senderName;
    private String senderPhoneNumber;
    private float amount;
    private Date date;
    private boolean transaction;

    public void setSenderName(String randomName) {
        this.senderName = randomName;
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    public void setSenderPhoneNumber(String randomPhoneNumber) {
        this.senderPhoneNumber = randomPhoneNumber;

    }

    public void setAmount(float randomAmount) {
        this.amount = randomAmount;

    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Object getSenderName() {
        return senderName;

    }

    public boolean getTransaction() {
        return transaction;
    }


    public Object getSenderPhoneNumber() {
        return senderPhoneNumber;

    }

    public Object getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    // Constructeur, getters et setters
}
