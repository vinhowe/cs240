package dao;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO extends BaseDAO<Person, String> {
  public PersonDAO(Connection conn) {
    super(conn, "Persons");
  }

  /**
   * Insert a person model into database
   *
   * @param person
   * @throws DataAccessException
   */
  @Override
  public void insert(Person person) throws DataAccessException {
    String sql =
        "INSERT INTO Persons (id, username, first_name, last_name, gender, "
            + "father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, person.getID());
      stmt.setString(2, person.getUsername());
      stmt.setString(3, person.getFirstName());
      stmt.setString(4, person.getLastName());
      stmt.setString(5, String.valueOf(person.getGender()));
      stmt.setString(6, person.getFatherID());
      stmt.setString(7, person.getMotherID());
      stmt.setString(8, person.getSpouseID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting into the database");
    }
  }

  /**
   * Attempt to find person with ID in database
   *
   * @param personID
   * @return matching person, if found
   * @throws DataAccessException
   */
  @Override
  public Person find(String personID) throws DataAccessException {
    Person person;
    ResultSet rs = null;
    String sql = "SELECT * FROM Persons WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, personID);
      rs = stmt.executeQuery();
      if (rs.next()) {
        person =
            new Person(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("gender").charAt(0),
                rs.getString("father_id"),
                rs.getString("mother_id"),
                rs.getString("spouse_id"));
        return person;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding person");
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

  /**
   * Attempt to find persons with given username in database
   *
   * @param username
   * @return matching persons
   * @throws DataAccessException
   */
  public List<Person> findPersonsForUser(String username) throws DataAccessException {
    List<Person> persons = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM Persons WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      while (rs.next()) {
        Person person =
            new Person(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("gender").charAt(0),
                rs.getString("father_id"),
                rs.getString("mother_id"),
                rs.getString("spouse_id"));
        persons.add(person);
      }
      return persons;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding persons");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void clearForUser(String username) throws DataAccessException {
    DAOUtil.clearForUser(username, tableName, conn);
  }
}
