package com.cbp.app.helper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LoggingHelper {
    public static LocalTime logStartOfMethod(String methodName) {
        LocalTime startTime = LocalTime.now();
        System.out.println(">>> [" + startTime.format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + methodName + " [START] >>>");
        return startTime;
    }

    public static void logEndOfMethod(String methodName, LocalTime startTime) {
        LocalTime endTime = LocalTime.now();
        long millisecondsBetween = ChronoUnit.MILLIS.between(startTime, endTime);
        System.out.println("<<< [" + endTime.format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + methodName + " [" + millisecondsBetween + " ms] <<<");
    }

    public static void logMessage(String message) {
        LocalTime startTime = LocalTime.now();
        System.out.println("=== [" + startTime.format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " + message);
    }
}
