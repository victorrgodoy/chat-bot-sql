package service;

import dev.langchain4j.model.ollama.OllamaChatModel;
import factory.ConnectionFactory;
import util.DBResultFormatter;
import java.sql.*;

public class OllamaService {

    public static String sendOllamaQuestion(String ddl, String question, String nameModel)  {

        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434") //host ollama server
                .modelName(nameModel)
                .temperature(0.4)
                .build();
        
        String instructions = "Task\n" +
                    "Generate a SQL query to answer [QUESTION]"+question+"[/QUESTION]\n" +
                    "Instructions\n" +
                    "If you cannot answer the question with the available database schema, return 'Eu não sei'. No explanation needed.\n" +
                    "Database Schema\n" +
                    "The query will run on a database with the following schema: "+ ddl + "\n" +
                    "Answer\n" +
                    "Given the database schema, here is the SQL query that answers [QUESTION]"+question+"[/QUESTION]\n" +
                    "[SQL] Your SQL query goes here[/SQL]";

        return model.generate(instructions);
    }

    public static String resultSQL(String schemaName, String querySQL) {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        DBResultFormatter formatter = new DBResultFormatter();

        String url = connectionFactory.getUrl();
        String user = connectionFactory.getUser();
        String password = connectionFactory.getPassword();

        try (Connection connection = DriverManager.getConnection(url + "/" + schemaName, user, password)){
            PreparedStatement statement = connection.prepareStatement(querySQL);
            ResultSet resultSet = statement.executeQuery();

            String result = formatter.FormatResult(resultSet);
            resultSet.close();
            statement.close();
            connection.close();
            return result;

        } catch (SQLException e) {
            System.out.println("Error filter result: " + e.getMessage());
            return "Erro.";
        }
    }

}