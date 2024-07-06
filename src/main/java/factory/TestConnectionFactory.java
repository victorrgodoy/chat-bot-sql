package factory;

public class TestConnectionFactory {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connectionFactory.getConnection();
    }
}