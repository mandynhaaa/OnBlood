package Connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Standard.Logger;

public class SQLGenerator {

    public static int insertSQL(String table, Map<String, String> data) {
        int lastId = 0;
        try (Connection conn = new ConnectionSQL().getConnection();
             Statement statement = conn.createStatement()) {

            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                columns.append(entry.getKey()).append(",");
                values.append("'").append(entry.getValue()).append("',");
            }

            if (columns.length() > 0) columns.setLength(columns.length() - 1);
            if (values.length() > 0) values.setLength(values.length() - 1);

            String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columns, values);
            Logger.generateLog("insertSQL: " + query);
            statement.executeUpdate(query);

            lastId = getLastId(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lastId;
    }

    public static boolean updateSQL(String table, int id, Map<String, String> data) {
        if (data == null || data.isEmpty()) {
            return false;
        }

        String idTabela = "id_" + table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase();

        try (Connection conn = new ConnectionSQL().getConnection();
             Statement statement = conn.createStatement()) {

            StringBuilder set = new StringBuilder();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                set.append(entry.getKey()).append(" = '").append(entry.getValue()).append("',");
            }

            if (set.length() > 0) set.setLength(set.length() - 1);

            String query = String.format("UPDATE %s SET %s WHERE %s = %d", table, set, idTabela, id);
            Logger.generateLog("updateSQL: " + query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean deleteSQL(String table, int id) {
        String idTabela = "id_" + table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase();

        try (Connection conn = new ConnectionSQL().getConnection();
             Statement statement = conn.createStatement()) {

            String query = String.format("DELETE FROM %s WHERE %s = %d", table, idTabela, id);
            Logger.generateLog("deleteSQL: " + query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static String[][] selectSQL(String table, List<String> columns, Map<String, String> filters) {
        List<String[]> resultList = new ArrayList<>();
        String[] columnNames = null;

        if (columns == null || columns.isEmpty()) {
            columns = new ArrayList<>();
            columns.add("*");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(String.join(", ", columns));
        sql.append(" FROM ").append(table);

        if (filters != null && !filters.isEmpty()) {
            sql.append(" WHERE ");
            List<String> conditions = new ArrayList<>();
            for (String key : filters.keySet()) {
                conditions.add(key + " = ?");
            }
            sql.append(String.join(" AND ", conditions));
        }

        try (Connection conn = new ConnectionSQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

        	String queryLog = sql.toString();
        	if (filters != null && !filters.isEmpty()) {
        	    for (String value : filters.values()) {
        	        queryLog = queryLog.replaceFirst("\\?", "'" + value.replace("'", "''") + "'");
        	    }
        	}
        	Logger.generateLog("selectSQL: " + queryLog);

            if (filters != null && !filters.isEmpty()) {
                int index = 1;
                for (String value : filters.values()) {
                    stmt.setString(index++, value);
                }
            }

            try (ResultSet resultSet = stmt.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                columnNames = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnNames[i - 1] = metaData.getColumnName(i);
                }

                while (resultSet.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getString(i);
                    }
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String[][] result = new String[resultList.size() + 1][columnNames.length];
        result[0] = columnNames;
        for (int i = 0; i < resultList.size(); i++) {
            result[i + 1] = resultList.get(i);
        }

        return result;
    }

    public static int getLastId(String table) {
        int lastId = 0;
        String idTabela = "id_" + table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase();

        try (Connection conn = new ConnectionSQL().getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(" + idTabela + ") AS lastId FROM " + table)) {

            Logger.generateLog("getLastId: SELECT MAX(" + idTabela + ") FROM " + table);

            if (resultSet.next()) {
                lastId = resultSet.getInt("lastId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastId;
    }
}