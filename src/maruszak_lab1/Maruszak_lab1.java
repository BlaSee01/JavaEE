/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maruszak_lab1;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public final class Maruszak_lab1 {
    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:./db/notebooks";
    public static final String QUERY = "select * from app.notebooks";

    private static java.sql.Connection conn;

    private Maruszak_lab1() { }

    public static boolean Connect() throws ClassNotFoundException, SQLException {
        conn = DriverManager.getConnection(JDBC_URL);
        if (conn == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean Disconnect() throws SQLException {
        if (conn == null) {
            return false;
        } else {
            conn.close();
            return true;
        }
    }

    public static String getData() throws SQLException {
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(QUERY);
        ResultSetMetaData rsmd = rs.getMetaData();
        String wiersz = new String();
        int colCount = rsmd.getColumnCount();

        for (int i = 1; i <= colCount; i++) {
            wiersz = wiersz.concat(rsmd.getColumnName(i) + "\t| ");
        }
        wiersz = wiersz.concat("\r\n");

        while (rs.next()) {
            for (int i = 1; i <= colCount; i++) {
                wiersz = wiersz.concat(rs.getString(i) + "\t| ");
            }
            wiersz = wiersz.concat("\r\n");
        }

        if (stat != null) {
            stat.close();
        }

        return wiersz;
    }
    
    public static boolean Update(int notebookID, String newName, int newPrice, int newPages, String newFormat, String newManufacturer, String hasNumberedPages) {
    
    String updateQuery = "UPDATE notebooks SET name = ?, price_pln = ?, pages = ?, format = ?, manufacturer = ?, has_numbered_pages = ? WHERE notebook_id = ?";

    try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {

        preparedStatement.setString(1, newName);
        preparedStatement.setInt(2, newPrice);
        preparedStatement.setInt(3, newPages);
        preparedStatement.setString(4, newFormat);
        preparedStatement.setString(5, newManufacturer);
        preparedStatement.setString(6, hasNumberedPages);

        preparedStatement.setInt(7, notebookID);

        int rowsAffected = preparedStatement.executeUpdate();

        return rowsAffected > 0;

    } catch (SQLException e) {

        e.printStackTrace();
        return false; 
    }
}
    
    public static boolean Insert(int notebookID,String newName, int newPrice, int newPages, String newFormat, String newManufacturer, String hasNumberedPages) {
    String insertQuery = "INSERT INTO notebooks (notebook_id, name, price_pln, pages, format, manufacturer, has_numbered_pages) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
        preparedStatement.setInt(1, notebookID);
        preparedStatement.setString(2, newName);
        preparedStatement.setInt(3, newPrice);
        preparedStatement.setInt(4, newPages);
        preparedStatement.setString(5, newFormat);
        preparedStatement.setString(6, newManufacturer);
        preparedStatement.setString(7, hasNumberedPages);
        
        int rowsAffected = preparedStatement.executeUpdate();

        return rowsAffected > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    public static boolean Delete(int notebookID) {
    String deleteQuery = "DELETE FROM notebooks WHERE notebook_id = ?";

    try (PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
        preparedStatement.setInt(1, notebookID);

        int rowsAffected = preparedStatement.executeUpdate();

        return rowsAffected > 0; 
    } catch (SQLException e) {
        e.printStackTrace();
        return false; 
    }
}

    
}
