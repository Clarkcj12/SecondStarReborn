package club.imaginears.secondstarreborn.utils;

import java.util.Objects;

/**
 * Represents a connection to a database with the necessary configuration details.
 * It is designed to support SQL, MongoDB, and RabbitMQ configurations.
 */
public class DatabaseConnection {
    private final String host;          // The database host
    private final int port;             // The port to connect to
    private final String username;      // The username for authentication
    private final String password;      // The password for authentication
    private final String databaseName;  // The name of the database

    /**
     * Constructor to initialize the database connection configuration.
     *
     * @param host         The database host.
     * @param port         The port used for the connection.
     * @param username     The username for the database connection.
     * @param password     The password for the database connection.
     * @param databaseName The specific database name to connect to.
     */
    public DatabaseConnection(String host, int port, String username, String password, String databaseName) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

    // Getters to retrieve database details

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Generates a JDBC-like connection string for SQL databases.
     * It can be overridden based on the specific database type (MongoDB, RabbitMQ, etc.).
     *
     * @return The connection string format "<host>:<port>/<databaseName>".
     */
    public String getConnectionUrl() {
        return String.format("%s:%d/%s", host, port, databaseName);
    }

    @Override
    public String toString() {
        return String.format(
                "DatabaseConnection{host='%s', port=%d, username='%s', database='%s'}",
                host,
                port,
                username,
                databaseName
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseConnection that = (DatabaseConnection) o;
        return port == that.port &&
                Objects.equals(host, that.host) &&
                Objects.equals(username, that.username) &&
                Objects.equals(databaseName, that.databaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, username, databaseName);
    }
}