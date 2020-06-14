package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private DateTimeUtil() {
    }

    public static <T extends Comparable<T>> boolean isBetween(T param, T start, T end) {
        return param.compareTo(start) >= 0 && param.compareTo(end) <= 0;
    }

    public static <T> T parse(String string, Class<T> tClass) {
        switch (tClass.getSimpleName()) {
            case ("LocalDate"):
                return (T) LocalDate.parse(string, DATE_FORMATTER);
            case ("LocalTime"):
                return (T) LocalTime.parse(string, TIME_FORMATTER);
            default:
                throw new IllegalStateException("Unexpected value: " + tClass.getSimpleName());
        }
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

