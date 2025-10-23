package com.example.stocki_client.ui.mainpage.portfolio.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.stocki_client.R;
import com.example.stocki_client.ui.mainpage.portfolio.PortfolioAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreatePortfolioDialog {

    public interface OnPortfolioCreatedListener {
        void onPortfolioCreated(String name, String note);
    }

    private final Context context;
    private PortfolioAdapter portfolioAdapter;
    private final OnPortfolioCreatedListener listener;

    public CreatePortfolioDialog(Context context, OnPortfolioCreatedListener listener, PortfolioAdapter portfolio) {
        this.context = context;
        this.listener = listener;
        this.portfolioAdapter = portfolio;
    }

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_portfolio, null);

        TextInputLayout layoutName = dialogView.findViewById(R.id.layoutPortfolioName);
        TextInputEditText etName = dialogView.findViewById(R.id.etPortfolioName);
        TextInputEditText etNote = dialogView.findViewById(R.id.etPortfolioNote);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MyAlertDialogTheme);
        builder.setTitle(context.getResources().getString(R.string.label_create_portfolio))
                .setView(dialogView)
                .setNegativeButton(context.getResources().getString(R.string.cancel_answer), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(context.getResources().getString(R.string.create_answer), null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.black));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String note = etNote.getText() != null ? etNote.getText().toString().trim() : "";

            String nameError = null;
            boolean valid = true;

            if (name.isEmpty()) {
                nameError = context.getResources().getString(R.string.error_empty_name);
                valid = false;
            } else if (portfolioAdapter.nameUsed(name)) {
                nameError = context.getResources().getString(R.string.error_name_used);
                valid = false;
            }

            layoutName.setError(nameError);

            if (valid) {
                listener.onPortfolioCreated(name, note);
                dialog.dismiss();
            }

        });
    }
}

