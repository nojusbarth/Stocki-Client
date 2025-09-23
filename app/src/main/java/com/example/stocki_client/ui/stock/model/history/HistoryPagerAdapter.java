package com.example.stocki_client.ui.stock.model.history;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class HistoryPagerAdapter extends FragmentStateAdapter {

    public static final int POSITION_STEP_1 = 0;
    public static final int POSITION_STEP_2 = 1;
    public static final int POSITION_STEP_3 = 2;


    public HistoryPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == POSITION_STEP_1) {
            return ModelHistoryFragment.newInstance(1);
        } else if(position == POSITION_STEP_2) {
            return ModelHistoryFragment.newInstance(2);
        } else if(position == POSITION_STEP_3){
            return ModelHistoryFragment.newInstance(3);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}