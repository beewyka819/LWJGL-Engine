package com.webs.beewyka819.gameengine.engine;

import static org.fusesource.jansi.Ansi.*;

public class ConsoleOutput {
    public static void printMessage(String message, Color color) {
        Clock.updateClock();
        int hour = Clock.getHour();
        int minute = Clock.getMinute();
        int second = Clock.getSecond();
        System.out.println(
            ansi().fg(color).a("[" + Clock.getMonth() + "/" + Clock.getDay()
                    + "/" + Clock.getYear() + " " + (hour > 12 ? hour - 12 : hour)
                    + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second) + (hour > 11 ? " PM]" : " AM]")
                    + " ").reset().a(message)
        );
    }

    public static void printError(String errorMessage, Color color) {
        Clock.updateClock();
        int hour = Clock.getHour();
        int minute = Clock.getMinute();
        int second = Clock.getSecond();
        System.err.println(
            ansi().fg(color).a("[" + Clock.getMonth() + "/" + Clock.getDay()
                    + "/" + Clock.getYear() + " " + (hour > 12 ? hour - 12 : hour)
                    + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second) + (hour > 11 ? " PM]" : " AM]")
                    + " ").reset().a(errorMessage)
        );
    }

    public static void printException(Exception excp, Color color) {
        Clock.updateClock();
        int hour = Clock.getHour();
        int minute = Clock.getMinute();
        int second = Clock.getSecond();
        System.err.print(
            ansi().fg(color).a("[" + Clock.getMonth() + "/" + Clock.getDay()
                    + "/" + Clock.getYear() + " " + (hour > 12 ? hour - 12 : hour)
                    + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second) + (hour > 11 ? " PM]" : " AM]")
                    + " ").reset()
        );
        excp.printStackTrace();
    }
}