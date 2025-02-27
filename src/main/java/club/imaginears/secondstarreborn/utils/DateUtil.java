package club.imaginears.secondstarreborn.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private static final String YEARS = " Year ";
    private static final String MONTHS = " Month ";
    private static final String DAYS = " Day ";
    private static final String HOURS = " Hour ";
    private static final String MINUTES = " Minute ";
    private static final String SECONDS = " Second ";
    private static final String NOW = "Now";

    private static final Pattern TIME_PATTERN = Pattern.compile(
            "(?:(\\d+)y[a-z]*[,\\s]*)?" +
                    "(?:(\\d+)mo[a-z]*[,\\s]*)?" +
                    "(?:(\\d+)w[a-z]*[,\\s]*)?" +
                    "(?:(\\d+)d[a-z]*[,\\s]*)?" +
                    "(?:(\\d+)h[a-z]*[,\\s]*)?" +
                    "(?:(\\d+)m[a-z]*[,\\s]*)?" +
                    "(?:(\\d+)s[a-z]*)?",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Formats the difference between two dates into a human-readable format.
     * The method computes the difference using the years, months, days, hours,
     * minutes, and seconds between the given dates, returning a string representation
     * of the largest two units of difference.
     *
     * @param from The start date and time.
     * @param to The end date and time.
     * @return A formatted string representing the difference between the two dates.
     */
    /**
     * Formats the difference between two dates into a human-readable format.
     *
     * @param startDate The start date and time.
     * @param endDate   The end date and time.
     * @return A formatted string representing the difference between the two dates.
     */
    public static String formatDateDiff(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.equals(endDate)) {
            return NOW;
        }

        boolean future = endDate.isAfter(startDate);
        if (!future) {
            LocalDateTime temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        StringBuilder result = new StringBuilder();
        calculateDifference(startDate, endDate, result);

        return result.toString().trim();
    }

    private static void calculateDifference(LocalDateTime startDate, LocalDateTime endDate, StringBuilder result) {
        int accuracy = 0;

        accuracy = appendTimeUnit(result, ChronoUnit.YEARS, startDate, endDate, accuracy, YEARS);
        accuracy = appendTimeUnit(result, ChronoUnit.MONTHS, startDate, endDate, accuracy, MONTHS);
        accuracy = appendTimeUnit(result, ChronoUnit.DAYS, startDate, endDate, accuracy, DAYS);
        accuracy = appendTimeUnit(result, ChronoUnit.HOURS, startDate, endDate, accuracy, HOURS);
        accuracy = appendTimeUnit(result, ChronoUnit.MINUTES, startDate, endDate, accuracy, MINUTES);
        appendTimeUnit(result, ChronoUnit.SECONDS, startDate, endDate, accuracy, SECONDS);
    }

    private static int appendTimeUnit(StringBuilder result, ChronoUnit unit, LocalDateTime startDate, LocalDateTime endDate, int accuracy, String unitString) {
        if (accuracy >= 2) return accuracy;

        long difference = unit.between(startDate, endDate);
        startDate = unit.addTo(startDate, difference);

        if (difference > 0) {
            result.append(difference).append(Math.abs(difference) > 1 ? unitString + "s " : unitString);
            accuracy++;
        }
        return accuracy;
    }


    /**
     * Formats the difference between a date and the current date.
     *
     * @param timestamp The timestamp to compare to the current date.
     * @return A formatted string representing the difference.
     */
    public static String formatDateDiff(long timestamp) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime comparisonDate = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, now.atZone(java.time.ZoneId.systemDefault()).getOffset());
        return formatDateDiff(now, comparisonDate);
    }

    /**
     * Parses a date difference string into a future or past timestamp.
     *
     * @param time   The time difference string.
     * @param future Whether the difference should be in the future or past.
     * @return The calculated timestamp in milliseconds since epoch.
     * @throws IllegalArgumentException If the input format is invalid.
     */
    public static long parseDateDiff(String time, boolean future) {
        Matcher matcher = TIME_PATTERN.matcher(time);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        long years = parseGroup(matcher, 1);
        long months = parseGroup(matcher, 2);
        long weeks = parseGroup(matcher, 3);
        long days = parseGroup(matcher, 4);
        long hours = parseGroup(matcher, 5);
        long minutes = parseGroup(matcher, 6);
        long seconds = parseGroup(matcher, 7);

        LocalDateTime dateTime = LocalDateTime.now();
        if (future) {
            dateTime = dateTime.plusYears(years).plusMonths(months).plusWeeks(weeks).plusDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        } else {
            dateTime = dateTime.minusYears(years).minusMonths(months).minusWeeks(weeks).minusDays(days).minusHours(hours).minusMinutes(minutes).minusSeconds(seconds);
        }

        // Enforce 10-year max difference
        LocalDateTime maxDate = LocalDateTime.now().plusYears(10);
        if (dateTime.isAfter(maxDate)) {
            return maxDate.toEpochSecond(java.time.ZoneOffset.UTC) * 1000;
        }
        return dateTime.toEpochSecond(java.time.ZoneOffset.UTC) * 1000;
    }

    private static long parseGroup(Matcher matcher, int groupIndex) {
        return matcher.group(groupIndex) != null ? Long.parseLong(matcher.group(groupIndex)) : 0;
    }
}