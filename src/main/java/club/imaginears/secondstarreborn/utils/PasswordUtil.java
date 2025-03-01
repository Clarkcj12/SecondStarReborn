package club.imaginears.secondstarreborn.utils;

import club.imaginears.secondstarreborn.utils.passwords.BCrypt;

public class PasswordUtil {

    /**
     * Hashes the password using the BCrypt algorithm.
     *
     * @param password The plain text password to hash.
     * @return The hashed password with a unique salt included.
     * @throws IllegalArgumentException if the password is null or empty.
     */
    public String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Validates a given plain text password against a hashed password.
     *
     * @param plain  The plain text password.
     * @param hashed The hashed password.
     * @return True if the password matches, false otherwise.
     * @throws IllegalArgumentException if either argument is null or empty.
     */
    public boolean validPassword(String plain, String hashed) {
        if (plain == null || plain.isEmpty() || hashed == null || hashed.isEmpty()) {
            throw new IllegalArgumentException("Inputs cannot be null or empty");
        }
        return BCrypt.checkpw(plain, hashed);
    }

    /**
     * Checks whether a password is "strong enough" based on general rules.
     *
     * @param plain The plain text password.
     * @return True if the password is strong, false otherwise.
     * @throws IllegalArgumentException if the password is null.
     */
    public boolean isStrongEnough(String plain) {
        if (plain == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        // At least 12 characters, one uppercase letter, one lowercase letter, one digit, one special character
        return plain.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*]).{12,}$");
    }
}