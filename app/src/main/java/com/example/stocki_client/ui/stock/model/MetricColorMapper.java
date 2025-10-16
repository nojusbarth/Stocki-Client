package com.example.stocki_client.ui.stock.model;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.example.stocki_client.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class MetricColorMapper {

    private static final List<Integer> COLORS = Arrays.asList(
            R.color.category_worst,
            R.color.category_bad,
            R.color.category_ok,
            R.color.category_good,
            R.color.category_super
    );

    private static final Map<String, double[]> DAILY_THRESHOLDS = new HashMap<>();
    private static final Map<String, double[]> HOURLY_THRESHOLDS = new HashMap<>();

    static {
        DAILY_THRESHOLDS.put("MAE", new double[]{1.0, 0.5, 0.2, 0.1});
        DAILY_THRESHOLDS.put("HIT", new double[]{50.0, 55.0, 60.0, 65.0});
        DAILY_THRESHOLDS.put("SHARPE", new double[]{0.2, 0.5, 1.0, 2.0});
        DAILY_THRESHOLDS.put("MAXDRAW", new double[]{20.0, 15.0, 10.0, 5.0});

        HOURLY_THRESHOLDS.put("MAE", new double[]{0.4, 0.2, 0.1, 0.05});
        HOURLY_THRESHOLDS.put("HIT", new double[]{50.0, 53.0, 57.0, 62.0});
        HOURLY_THRESHOLDS.put("SHARPE", new double[]{0.1, 0.2, 0.5, 1.0});
        HOURLY_THRESHOLDS.put("MAXDRAW", new double[]{10.0, 7.0, 5.0, 3.0});
    }

    public static int getFittingColor(String metricVal, String metricName, String interval, Context context) {
        if (metricVal == null || metricVal.isEmpty()) return R.color.light_gray;

        double value;
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(metricVal.trim());
            value = number.doubleValue();
        } catch (ParseException e) {
            return R.color.light_gray;
        }

        if (metricName.equals("MAXDRAW")) {
            value = Math.abs(value);

        }

        metricName = metricName.toUpperCase();
        interval = interval != null ? interval.toUpperCase() : "1D";

        // Intervalabhängige Grenzwerte wählen
        double[] limits = ("1H".equals(interval))
                ? HOURLY_THRESHOLDS.get(metricName)
                : DAILY_THRESHOLDS.get(metricName);

        if (limits == null) return R.color.light_gray;

        // Richtung bestimmen
        boolean higherIsBetter = metricName.equals("HIT") || metricName.equals("SHARPE");

        return ContextCompat.getColor(context, mapValueToColor(value, limits, higherIsBetter));
    }

    private static int mapValueToColor(double value, double[] limits, boolean higherIsBetter) {
        for (int i = 0; i < limits.length; i++) {
            if (higherIsBetter) {
                if (value < limits[i]) return COLORS.get(i);
            } else {
                if (value > limits[i]) return COLORS.get(i);
            }
        }
        return COLORS.get(COLORS.size() - 1);
    }
}

