package dao;

import model.AuthToken;
import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthTokenDAO extends BaseDAO<AuthToken, String> {
  public AuthTokenDAO(Connection conn) {
    super(conn, "Tokens");
  }

  /**
   * Insert an auth token into database
   *
   * @param authToken
   * @throws DataAccessException
   */
  @Override
  public void insert(AuthToken authToken) throws DataAccessException {
    String sql = "INSERT INTO Tokens (token, username) VALUES(?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authToken.getToken().toString());
      stmt.setString(2, authToken.getUsername());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting into the database");
    }
  }

  /**
   * Attempt to find auth token with value in database
   *
   * @param authToken
   * @return matching user, if found
   * @throws DataAccessException
   */
  @Override
  public AuthToken find(String authToken) throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT * FROM Tokens WHERE token = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authToken);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return new AuthToken(UUID.fromString(rs.getString("token")), rs.getString("username"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding token");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }
}
