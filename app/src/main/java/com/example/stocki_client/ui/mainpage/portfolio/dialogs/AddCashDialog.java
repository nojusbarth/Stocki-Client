package com.example.stocki_client.ui.mainpage.portfolio.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.stocki_client.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AddCashDialog {

    public interface OnCashAddedListener {
        void OnCashAdded(double amount);
    }


    private Context context;
    private OnCashAddedListener listener;

    public AddCashDialog(Context context, OnCashAddedListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void show() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_cash, null);
        TextInputLayout layoutCash = dialogView.findViewById(R.id.layoutCash);
        TextInputEditText etCash = dialogView.findViewById(R.id.etCash);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.label_add_cash))
                .setView(dialogView)
                .setNegativeButton(context.getResources().getString(R.string.cancel_answer), (d, which) -> d.dismiss())
                .setPositiveButton(context.getResources().getString(R.string.add_answer), null)
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.black));


        dialog.setOnShowListener(dialogInterface -> {
            Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnPositive.setOnClickListener(view -> {
                String cashInput = etCash.getText() != null ? etCash.getText().toString().trim() : "";

                if (cashInput.isEmpty()) {
                    layoutCash.setError(context.getResources().getString(R.string.error_no_sum));
                } else {
                    try {
                        String normalizedInput = cashInput.replace(',', '.');
                        double cashValue = Double.parseDouble(normalizedInput);

                        if (cashValue <= 0) {
                            layoutCash.setError(context.getResources().getString(R.string.error_negative_number));
                            return;
                        }

                        String[] parts = cashInput.split("\\.");
                        if (parts[0].length() > 7) {
                            layoutCash.setError(context.getResources().getString(R.string.error_big_number));
                            return;
                        }

                        BigDecimal bd = new BigDecimal(cashValue);
                        bd = bd.setScale(2, RoundingMode.DOWN);

                        layoutCash.setError(null);

                        listener.OnCashAdded(bd.doubleValue());
                        dialog.dismiss();

                    } catch (NumberFormatException e) {
                        layoutCash.setError(context.getResources().getString(R.string.error_invalid_number));
                    }
                }

            });
        });

    }

}
