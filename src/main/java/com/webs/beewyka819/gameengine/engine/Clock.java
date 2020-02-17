package com.webs.beewyka819.gameengine.engine;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class Clock {
    private static LocalDateTime now = LocalDateTime.now();

    public static void updateClock() {
        now = LocalDateTime.now();
    }

    public static int getYear() {
        return now.getYear();
    }

    public static int getMonth() {
        return now.getMonthValue();
    }

    public static int getDay() {
        return now.getDayOfMonth();
    }

    public static int getHour() {
        return now.getHour();
    }

    public static int getMinute() {
        return now.getMinute();
    }

    public static int getSecond() {
        return now.getSecond();
    }

    public static int getMillis() {
        return now.get(ChronoField.MICRO_OF_SECOND);
    }
}