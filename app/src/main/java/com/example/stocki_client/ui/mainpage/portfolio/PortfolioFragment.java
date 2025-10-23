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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.user.UserIdManager;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.ui.mainpage.portfolio.dialogs.CreatePortfolioDialog;
import com.example.stocki_client.ui.mainpage.predictions.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


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

        button.setOnClickListener(v -> new CreatePortfolioDialog(getContext(),
                (name, note) -> {
                    PortfolioData newPortfolio = new PortfolioData(name, note);
                    portfolioAdapter.addPortfolio(newPortfolio);
                    ApiClient.getInstance().createPortfolio(UserIdManager.getInstance(getContext()).getUserId(),
                            name, note);
                },portfolioAdapter ).show());
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
