package service;

import lombok.extern.slf4j.Slf4j;
import factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
public class DataBaseService {
    private final ConnectionFactory connectionFactory;

    public DataBaseService() {
        this.connectionFactory = ConnectionFactory.getInstance();
    }

    public ArrayList<String> listSchemaNames() {
        ArrayList<String> listSchema = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             ResultSet resultSets = connection.getMetaData().getCatalogs()) {

            while (resultSets.next()) {
                String schemaName = resultSets.getString(1);
                if (!isSystemSchema(schemaName)) {
                    listSchema.add(schemaName);
                }
            }
        } catch (SQLException e) {
            log.error("Error filtering schemas", e);
        }
        return listSchema;
    }

    public String generateDDL(String schemaName) {
        StringBuilder ddlStatements = new StringBuilder();

        String tablesQuery = "SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_SCHEMA = ?";
        String columnsQuery = "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE FROM information_schema.columns WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement tablesStmt = conn.prepareStatement(tablesQuery);
             PreparedStatement columnsStmt = conn.prepareStatement(columnsQuery)) {

            tablesStmt.setString(1, schemaName);
            ResultSet tables = tablesStmt.executeQuery();

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                ddlStatements.append("CREATE TABLE ").append(tableName).append(" (\n");

                columnsStmt.setString(1, schemaName);
                columnsStmt.setString(2, tableName);
                ResultSet columns = columnsStmt.executeQuery();
                boolean firstColumn = true;
                while (columns.next()) {
                    if (!firstColumn) {
                        ddlStatements.append(",\n");
                    }
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("DATA_TYPE");
                    boolean isNullable = "YES".equalsIgnoreCase(columns.getString("IS_NULLABLE"));
                    ddlStatements.append("  ").append(columnName).append(" ").append(dataType);
                    if (!isNullable) {
                        ddlStatements.append(" NOT NULL");
                    }
                    firstColumn = false;
                }
                columns.close();
                ddlStatements.append("\n").append(");\n\n");
            }
            tables.close();
        } catch (SQLException e) {
            log.error("Error generating DDL: {}", e.getMessage());
        }
        return ddlStatements.toString();
    }


    private boolean isSystemSchema(String schemaName) {
        return schemaName.equalsIgnoreCase("information_schema") ||
                schemaName.equalsIgnoreCase("mysql") ||
                schemaName.equalsIgnoreCase("performance_schema") ||
                schemaName.equalsIgnoreCase("sys");
    }
}

