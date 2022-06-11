package com.andrew121410.mc.world16essentials.utils;

public class StringDataTimeBuilder {

    public static String makeString(long theStartTime, long theEndTime) {
        StringBuilder stringBuilder = new StringBuilder();

        long timeElapsed = theEndTime - theStartTime;

        long days = timeElapsed / (1000 * 60 * 60 * 24);
        long hours = (timeElapsed % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timeElapsed % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeElapsed % (1000 * 60)) / 1000;

        if (days > 0) {
            if (days == 1) {
                stringBuilder.append(days).append(" day, ");
            } else {
                stringBuilder.append(days).append(" days, ");
            }
        }

        if (hours > 0) {
            if (hours == 1) {
                stringBuilder.append(hours).append(" hour, ");
            } else {
                stringBuilder.append(hours).append(" hours, ");
            }
        }

        if (minutes > 0) {
            if (minutes == 1) {
                stringBuilder.append(minutes).append(" minute, ");
            } else {
                stringBuilder.append(minutes).append(" minutes, ");
            }
            if (seconds > 0) {
                stringBuilder.append("and ");
            }
        }

        if (seconds > 0) {
            if (seconds == 1) {
                stringBuilder.append(seconds).append(" second");
            } else {
                stringBuilder.append(seconds).append(" seconds");
            }
        }

        return stringBuilder.toString();
    }
}
