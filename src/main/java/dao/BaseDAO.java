package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAO<T, ID> {
  protected final Connection conn;
  protected final String tableName;

  protected BaseDAO(Connection conn, String tableName) {
    this.conn = conn;
    this.tableName = tableName;
  }

  public abstract void insert(T t) throws DataAccessException;

  public abstract T find(ID id) throws DataAccessException;

  public void clear() throws DataAccessException {
    String sql = "DELETE FROM " + this.tableName;
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while clearing tables");
    }
  }
}
