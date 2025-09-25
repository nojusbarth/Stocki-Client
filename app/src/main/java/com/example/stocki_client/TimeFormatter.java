package com.example.stocki_client;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

    public String formatCV(String isoDate, String interval) {
        ZonedDateTime utcTime = ZonedDateTime.parse(isoDate);

        if ("1d".equals(interval)) {
            // Tageswerte: keine Konvertierung, nur Datum
            return utcTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } else if ("1h".equals(interval)) {
            // Stundenwerte: in lokale Zeitzone konvertieren
            ZonedDateTime localTime = utcTime.withZoneSameInstant(ZoneId.systemDefault());
            return localTime.format(DateTimeFormatter.ofPattern("dd-HH:mm:ss"));
        } else {
            // Fallback: einfach Original-ISO-String zurückgeben
            return isoDate;
        }
    }

    public String formatGraph(String isoDate, String interval) {
        ZonedDateTime utcTime = ZonedDateTime.parse(isoDate);

        if ("1d".equals(interval)) {
            // Tageswerte: keine Konvertierung, nur Datum
            return utcTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM"));
        } else if ("1h".equals(interval)) {
            // Stundenwerte: in lokale Zeitzone konvertieren
            ZonedDateTime localTime = utcTime.withZoneSameInstant(ZoneId.systemDefault());
            return localTime.format(DateTimeFormatter.ofPattern("dd-HH:mm"));
        } else {
            // Fallback: einfach Original-ISO-String zurückgeben
            return isoDate;
        }
    }

}
