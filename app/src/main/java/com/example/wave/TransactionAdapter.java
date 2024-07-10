package com.example.wave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private Context mContext;
    private List<Transaction> mTransactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
        mContext = context;
        mTransactions = transactions;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_transaction, parent, false);
        }

        Transaction currentTransaction = mTransactions.get(position);

        TextView senderInfo = listItem.findViewById(R.id.sender_info);
        boolean transaction = (boolean) currentTransaction.getTransaction();
        String transactionType = transaction ? "De" : "Vers";
        senderInfo.setText(String.format(Locale.getDefault(), "%s %s %s",transactionType, currentTransaction.getSenderName(), currentTransaction.getSenderPhoneNumber()));

        TextView amount = listItem.findViewById(R.id.amount);
        int color;
        if (transaction) {
            color = ContextCompat.getColor(mContext, R.color.transaction_color);
        } else {
            color = ContextCompat.getColor(mContext, R.color.red_color);
        }
        amount.setTextColor(color);
        String signType = transaction ? "" : "-";

        amount.setText(String.format(Locale.ENGLISH, "%s %.0f F",signType, currentTransaction.getAmount()));



        TextView date = listItem.findViewById(R.id.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        date.setText(dateFormat.format(currentTransaction.getDate()));

        return listItem;
    }
}