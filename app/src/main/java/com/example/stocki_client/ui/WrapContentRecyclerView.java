package com.example.stocki_client.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

// Java
public class WrapContentRecyclerView extends RecyclerView {
    public WrapContentRecyclerView(Context context) {
        super(context);
    }

    public WrapContentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandedHeightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandedHeightSpec);
    }
}

