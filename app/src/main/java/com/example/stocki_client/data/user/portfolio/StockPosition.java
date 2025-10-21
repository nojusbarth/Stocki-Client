package com.example.stocki_client.data.user.portfolio;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class StockPosition implements Parcelable {

    private String ticker;
    private double quantity;
    private double buyPrice;
    private double currentPrice;

    public StockPosition() {}

    public StockPosition(String ticker, double quantity, double buyPrice) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.currentPrice = buyPrice;
    }

    public StockPosition(StockPosition other) {
        this.ticker = other.ticker;
        this.quantity = other.quantity;
        this.buyPrice = other.buyPrice;
        this.currentPrice = other.currentPrice;
    }



    public void buyMore(double quantity, double price) {
        double totalCost = this.buyPrice * this.quantity + price * quantity;
        double totalQty = this.quantity + quantity;

        this.buyPrice = totalCost / totalQty;
        this.quantity = totalQty;
        this.currentPrice = price;

        this.buyPrice = round2(this.buyPrice);
        this.quantity = round2(this.quantity);
        this.currentPrice = round2(this.currentPrice);
    }

    public void sell(double quantityToSell, double sellPrice) {
        this.quantity -= quantityToSell;
        this.currentPrice = sellPrice;

        this.quantity = round2(this.quantity);
        this.currentPrice = round2(this.currentPrice);
    }

    private double round2(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public Map<String, Double> getMetrics() {

        Map<String, Double> metricsMap = new HashMap<>();
        metricsMap.put("absValue", this.currentPrice * this.quantity);
        metricsMap.put("absReturn", (this.currentPrice - this.buyPrice) * this.quantity);
        metricsMap.put("pctReturn", (this.buyPrice > 0) ? (this.currentPrice / this.buyPrice - 1.0) * 100.0 : 0.0);
        return metricsMap;
    }



    public String getTicker() { return ticker; }
    public double getQuantity() { return quantity; }
    public double getBuyPrice() { return buyPrice; }
    public double getCurrentPrice() { return currentPrice; }


    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ticker", ticker);
            obj.put("quantity", quantity);
            obj.put("buyPrice", buyPrice);
            obj.put("currentPrice", currentPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    protected StockPosition(Parcel in) {
        ticker = in.readString();
        quantity = in.readDouble();
        buyPrice = in.readDouble();
        currentPrice = in.readDouble();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticker);
        dest.writeDouble(quantity);
        dest.writeDouble(buyPrice);
        dest.writeDouble(currentPrice);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<StockPosition> CREATOR = new Parcelable.Creator<StockPosition>() {
        @Override
        public StockPosition createFromParcel(Parcel in) {
            return new StockPosition(in);
        }

        @Override
        public StockPosition[] newArray(int size) {
            return new StockPosition[size];
        }
    };
}


