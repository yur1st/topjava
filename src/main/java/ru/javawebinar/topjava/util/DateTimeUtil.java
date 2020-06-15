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

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T param, T start, T end) {
        return param.compareTo(start) >= 0 && param.compareTo(end) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalTime parseTime(String string) {
        return LocalTime.parse(string, TIME_FORMATTER);
    }

    public static LocalDate parseDate(String string) {
        return LocalDate.parse(string, DATE_FORMATTER);
    }
}

