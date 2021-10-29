package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOUtil {
  public static void clearForUser(String username, String table, Connection connection)
      throws DataAccessException {
    String sql = String.format("DELETE FROM %s WHERE username = ?;", table);
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, username);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("SQL error encountered while clearing table");
    }
  }
}
