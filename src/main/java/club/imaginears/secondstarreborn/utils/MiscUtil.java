package club.imaginears.secondstarreborn.utils;

import java.util.function.Predicate;

public class MiscUtil {

    private static final Predicate<String> IS_INT = s -> s != null && s.matches("-?\\d+");
    private static final Predicate<String> IS_DOUBLE = s -> s != null && s.matches("-?\\d+(\\.\\d+)?");

    /**
     * Checks if the string is an integer.
     *
     * @param s the string to check
     * @return true if the string can be parsed as an integer, false otherwise
     */
    public static boolean isInt(String s) {
        return IS_INT.test(s);
    }

    /**
     * Checks if the string is a double.
     *
     * @param s the string to check
     * @return true if the string can be parsed as a double, false otherwise
     */
    public static boolean isDouble(String s) {
        return IS_DOUBLE.test(s);
    }
}