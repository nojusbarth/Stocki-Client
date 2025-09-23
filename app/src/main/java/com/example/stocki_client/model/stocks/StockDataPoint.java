package com.example.stocki_client.model.stocks;
import android.os.Parcel;
import android.os.Parcelable;

public class StockDataPoint {

    private String date;
    private float close;

    public StockDataPoint() {}



    public String getDate() { return date; }
    public float getClose() { return close; }


}

