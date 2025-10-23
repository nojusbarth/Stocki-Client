package com.example.stocki_client.data.user.portfolio;
import java.time.Instant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class PortfolioData implements Parcelable {

    public static final String ABS_VALUE = "absValue";
    public static final String ABS_RETURN = "absReturn";
    public static final String PCT_RETURN = "pctReturn";


    private String name;
    private String note;
    private String createdAt;
    private double cash;
    private Map<String, StockPosition> stockPositions = new HashMap<>();

    public PortfolioData() {}

    public PortfolioData(String name, String note) {
        this.name = name;
        this.note = note;
        this.cash = 0.0;
        this.createdAt = Instant.now().toString();
    }

    public PortfolioData(PortfolioData other) {
        this.name = other.name;
        this.note = other.note;
        this.createdAt = other.createdAt;
        this.cash = other.cash;

        this.stockPositions = new HashMap<>();
        for (Map.Entry<String, StockPosition> entry : other.stockPositions.entrySet()) {
            this.stockPositions.put(entry.getKey(), new StockPosition(entry.getValue()));
        }
    }


    public StockPosition buyStock(String ticker, double investAmount, double currentPrice) {

        double quantity = investAmount / currentPrice;

        StockPosition position = stockPositions.get(ticker);
        if (position == null) {
            position = new StockPosition(ticker, quantity, currentPrice);
            stockPositions.put(ticker, position);
        } else {
            position.buyMore(quantity, currentPrice);
        }

        cash -= investAmount;

        return position;
    }

    public StockPosition sellStock(String ticker, double sellAmount, double sellPrice) {
        StockPosition position = stockPositions.get(ticker);
        if (position == null) {
            throw new IllegalArgumentException("Stock nicht im Portfolio");
        }

        double quantityToSell = sellAmount / sellPrice;


        position.sell(quantityToSell, sellPrice);

        cash += sellAmount;

        if (position.getQuantity() <= 1e-9) {
            stockPositions.remove(ticker);
            return null;
        }

        return position;
    }


    public Map<String, Double> getMetrics() {
        double totalBuyin = 0.0;
        double totalValueStocks = 0.0;

        for (StockPosition s : stockPositions.values()) {
            totalBuyin += s.getBuyPrice() * s.getQuantity();
            totalValueStocks += s.getCurrentPrice() * s.getQuantity();
        }

        Map<String, Double> metricMap = new HashMap<>();

        metricMap.put(ABS_VALUE, totalValueStocks);
        metricMap.put(ABS_RETURN, totalValueStocks- totalBuyin);
        metricMap.put(PCT_RETURN, (totalBuyin > 0) ? (metricMap.get(ABS_RETURN) / totalBuyin) * 100.0 : 0.0);

        return metricMap;
    }

    public String getName() { return name; }
    public String getNote() { return note; }
    public String getCreatedAt() { return createdAt; }

    public double getCash() { return cash; }
    public void setCash(double cash) { this.cash = cash; }

    public List<StockPosition> getStockPositions() {
        List<StockPosition> out = new ArrayList<>();

        for (String ticker : stockPositions.keySet()) {
            out.add(stockPositions.get(ticker));
        }

        return out;
    }



    protected PortfolioData(Parcel in) {
        name = in.readString();
        note = in.readString();
        createdAt = in.readString();
        cash = in.readDouble();


        int mapSize = in.readInt();
        stockPositions = new HashMap<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            String key = in.readString();
            StockPosition value = in.readParcelable(StockPosition.class.getClassLoader());
            if (key != null && value != null) {
                stockPositions.put(key, value);
            }
        }
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(note);
        dest.writeString(createdAt);
        dest.writeDouble(cash);

        dest.writeInt(stockPositions.size());
        for (Map.Entry<String, StockPosition> entry : stockPositions.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PortfolioData> CREATOR = new Creator<PortfolioData>() {
        @Override
        public PortfolioData createFromParcel(Parcel in) {
            return new PortfolioData(in);
        }

        @Override
        public PortfolioData[] newArray(int size) {
            return new PortfolioData[size];
        }
    };

}



