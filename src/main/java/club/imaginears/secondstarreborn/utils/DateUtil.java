package club.imaginears.secondstarreborn.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

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
    public static String formatDateDiff(LocalDateTime from, LocalDateTime to) {
        if (from.equals(to)) {
            return "Now";
        }

        boolean future = to.isAfter(from);
        if (!future) {
            // Swap if `from` is after `to` to calculate positive differences
            LocalDateTime temp = from;
            from = to;
            to = temp;
        }

        StringBuilder result = new StringBuilder();

        long years = ChronoUnit.YEARS.between(from, to);
        from = from.plusYears(years);

        long months = ChronoUnit.MONTHS.between(from, to);
        from = from.plusMonths(months);

        long days = ChronoUnit.DAYS.between(from, to);
        from = from.plusDays(days);

        long hours = ChronoUnit.HOURS.between(from, to);
        from = from.plusHours(hours);

        long minutes = ChronoUnit.MINUTES.between(from, to);
        from = from.plusMinutes(minutes);

        long seconds = ChronoUnit.SECONDS.between(from, to);

        int accuracy = 0;
        if (years > 0 && accuracy < 2) {
            result.append(years).append(years > 1 ? " Years " : " Year ");
            accuracy++;
        }
        if (months > 0 && accuracy < 2) {
            result.append(months).append(months > 1 ? " Months " : " Month ");
            accuracy++;
        }
        if (days > 0 && accuracy < 2) {
            result.append(days).append(days > 1 ? " Days " : " Day ");
            accuracy++;
        }
        if (hours > 0 && accuracy < 2) {
            result.append(hours).append(hours > 1 ? " Hours " : " Hour ");
            accuracy++;
        }
        if (minutes > 0 && accuracy < 2) {
            result.append(minutes).append(minutes > 1 ? " Minutes " : " Minute ");
            accuracy++;
        }
        if (seconds > 0 && accuracy < 2) {
            result.append(seconds).append(seconds > 1 ? " Seconds " : " Second ");
        }

        return result.toString().trim();
    }

    /**
     * Formats the difference between a date and the current date into a human-readable format.
     *
     * @param date The date and time to compare to the current date.
     * @return A formatted string representing the difference.
     */
    public static String formatDateDiff(long date) {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.ofEpochSecond(date / 1000, 0, from.atZone(java.time.ZoneId.systemDefault()).getOffset());
        return formatDateDiff(from, to);
    }

    /**
     * Parses a date difference string into a future or past timestamp.
     *
     * @param time   The time difference string (e.g., "2y 3mo 4d").
     * @param future Whether the difference should be in the future or past.
     * @return The calculated timestamp in milliseconds since epoch.
     * @throws IllegalArgumentException If the input format is invalid.
     */
    public static long parseDateDiff(String time, boolean future) {
        Pattern timePattern = Pattern.compile(
                "(?:(\\d+)y[a-z]*[,\\s]*)?" +
                        "(?:(\\d+)mo[a-z]*[,\\s]*)?" +
                        "(?:(\\d+)w[a-z]*[,\\s]*)?" +
                        "(?:(\\d+)d[a-z]*[,\\s]*)?" +
                        "(?:(\\d+)h[a-z]*[,\\s]*)?" +
                        "(?:(\\d+)m[a-z]*[,\\s]*)?" +
                        "(?:(\\d+)s[a-z]*)?",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = timePattern.matcher(time);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        long years = matcher.group(1) != null ? Long.parseLong(matcher.group(1)) : 0;
        long months = matcher.group(2) != null ? Long.parseLong(matcher.group(2)) : 0;
        long weeks = matcher.group(3) != null ? Long.parseLong(matcher.group(3)) : 0;
        long days = matcher.group(4) != null ? Long.parseLong(matcher.group(4)) : 0;
        long hours = matcher.group(5) != null ? Long.parseLong(matcher.group(5)) : 0;
        long minutes = matcher.group(6) != null ? Long.parseLong(matcher.group(6)) : 0;
        long seconds = matcher.group(7) != null ? Long.parseLong(matcher.group(7)) : 0;

        LocalDateTime dateTime = LocalDateTime.now();
        if (future) {
            dateTime = dateTime.plusYears(years)
                    .plusMonths(months)
                    .plusWeeks(weeks)
                    .plusDays(days)
                    .plusHours(hours)
                    .plusMinutes(minutes)
                    .plusSeconds(seconds);
        } else {
            dateTime = dateTime.minusYears(years)
                    .minusMonths(months)
                    .minusWeeks(weeks)
                    .minusDays(days)
                    .minusHours(hours)
                    .minusMinutes(minutes)
                    .minusSeconds(seconds);
        }

        // Enforce a 10-year maximum difference
        LocalDateTime maxDate = LocalDateTime.now().plusYears(10);
        if (dateTime.isAfter(maxDate)) {
            return maxDate.toEpochSecond(java.time.ZoneOffset.UTC) * 1000;
        }

        return dateTime.toEpochSecond(java.time.ZoneOffset.UTC) * 1000;
    }
}