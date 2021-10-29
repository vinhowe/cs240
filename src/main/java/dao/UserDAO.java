package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends BaseDAO<User, String> {
  public UserDAO(Connection conn) {
    super(conn, "Users");
  }

  /**
   * Insert a user model into database
   *
   * @param user
   * @throws DataAccessException
   */
  @Override
  public void insert(User user) throws DataAccessException {
    String sql =
        "INSERT INTO Users (username, password, email, first_name, "
            + "last_name, gender, person_id) VALUES(?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getFirstName());
      stmt.setString(5, user.getLastName());
      stmt.setString(6, String.valueOf(user.getGender()));
      stmt.setString(7, user.getPersonID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting into the database");
    }
  }

  /**
   * Attempt to find user with ID in database
   *
   * @param username
   * @return matching user, if found
   * @throws DataAccessException
   */
  @Override
  public User find(String username) throws DataAccessException {
    User user;
    ResultSet rs = null;
    String sql = "SELECT * FROM Users WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      if (rs.next()) {
        user =
            new User(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("gender").charAt(0),
                rs.getString("person_id"));
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding user");
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

  public void clearForUser(String username) throws DataAccessException {
    DAOUtil.clearForUser(username, tableName, conn);
  }
}
