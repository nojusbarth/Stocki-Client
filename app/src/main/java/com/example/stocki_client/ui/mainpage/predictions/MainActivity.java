package com.example.stocki_client.ui.mainpage.predictions;


import android.os.Bundle;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocki_client.R;

import com.example.stocki_client.ui.mainpage.portfolio.PortfolioFragment;
import com.example.stocki_client.ui.mainpage.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.loadData();

        initPager();

    }


    private void initPager() {
        ViewPager2 viewPager = findViewById(R.id.viewPagerMain);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavMain);
        viewPager.setUserInputEnabled(false);

        List<Fragment> fragments = Arrays.asList(
                new MainFragment(),
                new SearchFragment(),
                new PortfolioFragment()
        );

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_search) {
                viewPager.setCurrentItem(1, true);
                return true;
            }else if (itemId == R.id.nav_portfolio) {
                viewPager.setCurrentItem(2, true);
                return true;
            }

            return false;
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNav.getMenu().getItem(position).setChecked(true);
            }
        });
    }
}