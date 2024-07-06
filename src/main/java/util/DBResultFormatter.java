package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DBResultFormatter {
    public String FormatResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metadata = resultSet.getMetaData();
        int count = metadata.getColumnCount();
        StringBuilder columns = new StringBuilder();
        StringBuilder rows = new StringBuilder();
        if (resultSet.isLast()) {
            return "Nenhum resultado encontrado.";
        }
        if (count > 1) {
            for (int i = 1; i <= count; i++) {
                columns.append("<th>").append(metadata.getColumnName(i)).append("</th>");
            }
            while (resultSet.next()) {
                rows.append("<tr>");
                for (int i = 1; i <= count; i++) {
                    rows.append("<td>").append(resultSet.getString(i)).append("</td>");
                }
                rows.append("</tr>");
            }
            return "<table>" + columns + rows + "</table>";
        } else {
            while (resultSet.next()) {
                rows.append(resultSet.getString(1)).append(resultSet.isLast() ? "." : ", ");
            }
            return rows.toString();
        }
    }
}
