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

    private static final Map<String, double[]> HOURLY_THRESHOLDS = new HashMap<>();
    private static final Map<String, double[]> DAILY_THRESHOLDS = new HashMap<>();
    private static final Map<String, double[]> TEN_DAY_THRESHOLDS = new HashMap<>();

    static {
        HOURLY_THRESHOLDS.put("MAE", new double[]{0.4, 0.2, 0.1, 0.05});
        HOURLY_THRESHOLDS.put("HIT", new double[]{50.0, 53.0, 57.0, 62.0});
        HOURLY_THRESHOLDS.put("SHARPE", new double[]{0.1, 0.2, 0.5, 1.0});
        HOURLY_THRESHOLDS.put("MAXDRAW", new double[]{10.0, 7.0, 5.0, 3.0});
        HOURLY_THRESHOLDS.put("CWR", new double[]{0.2, 0.5, 0.9, 1.5});
        HOURLY_THRESHOLDS.put("COVERAGE", new double[]{55.0, 65.0, 75.0, 85.0});

        DAILY_THRESHOLDS.put("MAE", new double[]{1.0, 0.5, 0.2, 0.1});
        DAILY_THRESHOLDS.put("HIT", new double[]{50.0, 55.0, 60.0, 65.0});
        DAILY_THRESHOLDS.put("SHARPE", new double[]{0.2, 0.5, 1.0, 2.0});
        DAILY_THRESHOLDS.put("MAXDRAW", new double[]{20.0, 15.0, 10.0, 5.0});
        DAILY_THRESHOLDS.put("CWR", new double[]{0.3, 0.7, 1.2, 2.0});
        DAILY_THRESHOLDS.put("COVERAGE", new double[]{60.0, 70.0, 80.0, 90.0});

        TEN_DAY_THRESHOLDS.put("MAE", new double[]{2.0, 1.0, 0.5, 0.25});
        TEN_DAY_THRESHOLDS.put("HIT", new double[]{50.0, 54.0, 58.0, 63.0});
        TEN_DAY_THRESHOLDS.put("SHARPE", new double[]{0.3, 0.6, 1.0, 1.8});
        TEN_DAY_THRESHOLDS.put("MAXDRAW", new double[]{25.0, 18.0, 12.0, 7.0});
        TEN_DAY_THRESHOLDS.put("CWR", new double[]{0.4, 0.8, 1.5, 2.5});
        TEN_DAY_THRESHOLDS.put("COVERAGE", new double[]{65.0, 75.0, 85.0, 92.0});
    }

    public static Map<String, double[]> getThresholdsForInterval(String interval) {
        if (interval == null) return DAILY_THRESHOLDS;

        switch (interval.toLowerCase()) {
            case "1h":
            case "4h":
            case "hourly":
                return HOURLY_THRESHOLDS;
            case "10d":
                return TEN_DAY_THRESHOLDS;
            default:
                return DAILY_THRESHOLDS;
        }
    }

    public static int getFittingColor(String metricVal, String metricName, String interval, Context context) {
        if (metricVal == null || metricVal.isEmpty())
            return ContextCompat.getColor(context, R.color.light_gray);

        double value;
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(metricVal.trim());
            value = number.doubleValue();
        } catch (ParseException e) {
            return ContextCompat.getColor(context, R.color.light_gray);
        }

        metricName = metricName.toUpperCase(Locale.ROOT);
        interval = (interval != null ? interval.toUpperCase(Locale.ROOT) : "1D");

        if (metricName.equals("MAXDRAW")) {
            value = Math.abs(value);
        }

        Map<String, double[]> thresholds = getThresholdsForInterval(interval);
        double[] limits = thresholds.get(metricName);

        boolean higherIsBetter = isHigherBetterMetric(metricName);

        return ContextCompat.getColor(context, mapValueToColor(value, limits, higherIsBetter));
    }

    private static boolean isHigherBetterMetric(String metricName) {
        return metricName.equals("HIT") ||
                metricName.equals("SHARPE") ||
                metricName.equals("COVERAGE");
    }

    private static int mapValueToColor(double value, double[] limits, boolean higherIsBetter) {
        if (higherIsBetter) {
            if (value < limits[0]) return COLORS.get(0);
            else if (value < limits[1]) return COLORS.get(1);
            else if (value < limits[2]) return COLORS.get(2);
            else if (value < limits[3]) return COLORS.get(3);
            else return COLORS.get(4);
        } else {
            if (value > limits[0]) return COLORS.get(0);
            else if (value > limits[1]) return COLORS.get(1);
            else if (value > limits[2]) return COLORS.get(2);
            else if (value > limits[3]) return COLORS.get(3);
            else return COLORS.get(4);
        }
    }
}

