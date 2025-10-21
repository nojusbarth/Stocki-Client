package com.example.stocki_client.ui.mainpage.portfolio;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.stocki_client.R;

import com.example.stocki_client.TimeFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class PortfolioInfoSheet extends BottomSheetDialogFragment {

    private static final String ARG_CREATION = "creation_date";
    private static final String ARG_NOTE = "note";


    public static PortfolioInfoSheet newInstance(String creationDate, String note) {
        PortfolioInfoSheet fragment = new PortfolioInfoSheet();
        Bundle args = new Bundle();
        args.putString(ARG_CREATION, creationDate);
        args.putString(ARG_NOTE, note);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_portfolio, container, false);

        if (getArguments() != null) {
            TextView txtCreationDate = view.findViewById(R.id.txtPortfolioCreatedAt);

            TimeFormatter timeFormatter = new TimeFormatter();
            String formatTime = timeFormatter.formatCV(getArguments().getString(ARG_CREATION),"1d");

            txtCreationDate.setText(String.format(Locale.getDefault(), "Created at:%s", formatTime));

            String note = getArguments().getString(ARG_NOTE);
            if (!note.isEmpty()) {
                TextView txtNote = view.findViewById(R.id.txtPortfolioNote);
                txtNote.setText(note);
            }
        }


        return view;
    }


}