package com.example.stocki_client.ui.mainpage.portfolio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.user.UserIdManager;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.ui.mainpage.predictions.MainActivityViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class PortfolioFragment extends Fragment {

    private MainActivityViewModel viewModel;

    private PortfolioAdapter portfolioAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        initPreviews(view);
        initAddButton(view);
        return view;
    }


    private void initPreviews(View view) {

        RecyclerView portView = view.findViewById(R.id.rvPortfolios);

        portView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        portfolioAdapter = new PortfolioAdapter(getContext(), portfolio -> {
            Intent intent = new Intent(getContext(), ShowPortfolioActivity.class);
            intent.putExtra("portfolioData", portfolio);
            portfolioLauncher.launch(intent);
        });

        portView.setAdapter(portfolioAdapter);

        viewModel.getPortfolios().observe(getViewLifecycleOwner(), data -> {
            portfolioAdapter.setData(data);
        });

    }



    private void initAddButton(View view) {

        FloatingActionButton button = view.findViewById(R.id.btnAddPortfolio);

        button.setOnClickListener(v -> showCreatePortfolioDialog());
    }

    private void newPortfolioCreated(String name, String note) {
        PortfolioData newPortfolio = new PortfolioData(name, note);
        portfolioAdapter.addPortfolio(newPortfolio);
        ApiClient.getInstance().createPortfolio(UserIdManager.getInstance(getContext()).getUserId(),
                name, note);
    }


    private void showCreatePortfolioDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_create_portfolio, null);

        TextInputLayout layoutName = dialogView.findViewById(R.id.layoutPortfolioName);
        TextInputLayout layoutNote = dialogView.findViewById(R.id.layoutPortfolioNote);
        TextInputEditText etName = dialogView.findViewById(R.id.etPortfolioName);
        TextInputEditText etNote = dialogView.findViewById(R.id.etPortfolioNote);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Neues Portfolio erstellen")
                .setView(dialogView)
                .setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Erstellen", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String note = etNote.getText() != null ? etNote.getText().toString().trim() : "";

            boolean valid = true;

            if (name.isEmpty()) {
                layoutName.setError("Name darf nicht leer sein");
                valid = false;
            } else if (portfolioAdapter.nameUsed(name)) {
                layoutName.setError("Name wird bereits verwendet");
                valid = false;
            } else {
                layoutName.setError(null);
            }

            if (note.length() > 100) {
                layoutNote.setError("Notiz zu lang (max. 100 Zeichen)");
                valid = false;
            } else {
                layoutNote.setError(null);
            }

            if (valid) {
                newPortfolioCreated(name, note);
                dialog.dismiss();
            }
        });
    }

    private final ActivityResultLauncher<Intent> portfolioLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                PortfolioData updated = data.getParcelableExtra("updatedPortfolio");
                                if (updated != null) {
                                    portfolioAdapter.updatePortfolio(updated);
                                }
                            }
                        }
                    });

}
