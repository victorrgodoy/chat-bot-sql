package factory;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import util.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Getter
public class ConnectionFactory {
    private static final ConnectionFactory INSTANCE = new ConnectionFactory();
    private String url;
    private String user;
    private String password;
    private Connection connection;

    private ConnectionFactory() {
        initialize();
    }

    public static ConnectionFactory getInstance(){
        return INSTANCE;
    }

    private void initialize() {
        PropertiesLoader propertiesLoader = new PropertiesLoader("src/main/resources/config.properties");
        this.url = propertiesLoader.getProperty("url");
        this.user = propertiesLoader.getProperty("user");
        this.password = propertiesLoader.getProperty("password");
    }

    @Synchronized
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            log.error("Error getting database connection", e);
            throw new RuntimeException("Failed to obtain database connection", e);
        }
        return connection;
    }

}



