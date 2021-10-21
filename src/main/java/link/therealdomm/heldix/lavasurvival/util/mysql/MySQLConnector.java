package link.therealdomm.heldix.lavasurvival.util.mysql;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * the connector class that handles all mysql stuff
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class MySQLConnector {

    private static final String URL = "jdbc:mysql://%s:%d/%s";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private final AtomicBoolean openable = new AtomicBoolean(false);
    private final List<Connection> pendingConnections = new ArrayList<>();
    private final MySQLData data;
    private final ExecutorService executorService;

    /**
     * the default constructor
     * @param data with the database credentials
     */
    public MySQLConnector(MySQLData data) {
        this.data = data;
        try {
            Class.forName(DRIVER_CLASS);
            Connection connection;
            if ((connection = this.getConnection()) != null) {
                this.closeConnection(connection);
                this.openable.set(true);
            } else {
                throw new IllegalStateException("Could not open connection to the database! Please check you credentials.");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not find mysql driver!");
        }
        final ThreadFactory builder = (Runnable r) -> {
            final Thread thread = new Thread(r, "Async Database Thread");
            thread.setDaemon(true);
            return thread;
        };
        this.executorService = Executors.newSingleThreadExecutor(builder);
    }

    /**
     * creates a result class of the given sql query
     * @param sql the query string
     * @param replacements to replace ? to a value
     * @return the result
     */
    public MySQLResult query(String sql, Object... replacements) {
        if (!this.openable.get()) {
            throw new IllegalStateException("Can not open any database connection!");
        }
        try {
            Connection connection = this.getConnection();
            if (connection == null) {
                throw new IllegalStateException("Can not open the connection to the database!");
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < replacements.length; i++) {
                preparedStatement.setObject(i+1, replacements[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            MySQLResult mySQLResult = MySQLResult.build(resultSet);
            preparedStatement.close();
            this.closeConnection(connection);
            return mySQLResult;
        } catch (SQLException e) {
            LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * dispatches a query async
     * @param resultConsumer when result received
     * @param sql the query string
     * @param replacements to replace ? to a value
     */
    public void asyncQuery(Consumer<MySQLResult> resultConsumer, String sql, Object... replacements) {
        this.executorService.execute(() -> {
            try {
                MySQLResult query = this.query(sql, replacements);
                resultConsumer.accept(query);
            } catch (Exception e) {
                LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
                resultConsumer.accept(null);
            }
        });
    }

    /**
     * perform an update statement to the database
     * @param sql the query string
     * @param replacements to replace ? to a value
     */
    public void update(String sql, Object... replacements) {
        if (!this.openable.get()) {
            throw new IllegalStateException("Can not open any database connection!");
        }
        try {
            Connection connection = this.getConnection();
            if (connection == null) {
                throw new IllegalStateException("Can not open the connection to the database!");
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < replacements.length; i++) {
                preparedStatement.setObject(i+1, replacements[i]);
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            this.closeConnection(connection);
        } catch (SQLException e) {
            LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
    }

    /**
     * perform an async update
     * @param sql the query string
     * @param replacements to replace ? to a value
     */
    public void asyncUpdate(String sql, Object... replacements) {
        this.executorService.execute(() -> {
            try {
                this.update(sql, replacements);
            } catch (Exception e) {
                LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
            }
        });
    }

    /**
     * closes all pending connections and blocks the creation of new connections
     */
    public void shutdown() {
        this.openable.set(false);
        this.closeConnections();
    }

    /**
     * creates a new connection
     * @return null if connection could not be opened
     */
    private Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    String.format(URL, this.data.getHostname(), this.data.getPort(), this.data.getDatabase()),
                    this.data.getUsername(),
                    this.data.getPassword()
            );
            if (connection != null) {
                this.pendingConnections.add(connection);
                return connection;
            }
        } catch (SQLException e) {
            LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * closes the specified connection
     * @param connection to be closed
     */
    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                this.pendingConnections.remove(connection);
            } catch (SQLException e) {
                LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
            }
        }
    }

    /**
     * closes all pending connections
     */
    private void closeConnections() {
        for (Connection pendingConnection : this.pendingConnections) {
            try {
                pendingConnection.close();
            } catch (SQLException e) {
                LavaSurvivalPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
            }
        }
        this.pendingConnections.clear();
    }

}
