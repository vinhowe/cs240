package dao;

import java.sql.*;

public class Database {
  private Connection conn;

  public Connection openConnection(boolean autoCommit) throws DataAccessException {
    try {
      final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

      conn = DriverManager.getConnection(CONNECTION_URL);

      conn.setAutoCommit(autoCommit);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Unable to open connection to database");
    }

    return conn;
  }

  public Connection getConnection(boolean autoCommit) throws DataAccessException {
    if (conn == null) {
      return openConnection(autoCommit);
    } else {
      return conn;
    }
  }

  public Connection getConnection() throws DataAccessException {
    return getConnection(false);
  }

  public void closeConnection(boolean commit) throws DataAccessException {
    try {
      if (commit) {
        conn.commit();
      } else {
        conn.rollback();
      }

      conn.close();
      conn = null;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Unable to close database connection");
    }
  }

  public void clearTable(String table) throws DataAccessException {
    String sql = "DELETE FROM " + table;
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while clearing tables");
    }
  }
}
