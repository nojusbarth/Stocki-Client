package com.example.stocki_client.ui.mainpage.portfolio.dialogs;

import android.content.Context;


import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.stocki_client.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DeletePortfolioDialog {

    public interface OnPortfolioDeletedListener {
        void onDeleteConfirmed(int position);
    }

    private final Context context;
    private final OnPortfolioDeletedListener listener;
    private final int position;

    public DeletePortfolioDialog(Context context, OnPortfolioDeletedListener listener, int position) {
        this.context = context;
        this.listener = listener;
        this.position = position;
    }

    public void show() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(context.getResources().getString(R.string.label_delete_portfolio))
                .setMessage(context.getResources().getString(R.string.info_delete))
                .setNegativeButton(context.getResources().getString(R.string.cancel_answer), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(context.getResources().getString(R.string.delete_answer), null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.black));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            listener.onDeleteConfirmed(position);
            dialog.dismiss();
        });

    }
}
