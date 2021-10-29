package dao;

import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO extends BaseDAO<Event, String> {
  public EventDAO(Connection conn) {
    super(conn, "Events");
  }

  /**
   * Insert an event model into database
   *
   * @param event
   * @throws DataAccessException
   */
  @Override
  public void insert(Event event) throws DataAccessException {
    String sql =
        "INSERT INTO Events (id, username, person_id, latitude, longitude, "
            + "country, city, year, event_type) VALUES(?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, event.getID());
      stmt.setString(2, event.getUsername());
      stmt.setString(3, event.getPersonID());
      stmt.setDouble(4, event.getLatitude());
      stmt.setDouble(5, event.getLongitude());
      stmt.setString(6, event.getCountry());
      stmt.setString(7, event.getCity());
      stmt.setInt(8, event.getYear());
      stmt.setString(9, event.getEventType());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting into the database");
    }
  }

  /**
   * Attempt to find event with ID in database
   *
   * @param eventID
   * @return matching event, if found
   * @throws DataAccessException
   */
  @Override
  public Event find(String eventID) throws DataAccessException {
    Event event;
    ResultSet rs = null;
    String sql = "SELECT * FROM Events WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, eventID);
      rs = stmt.executeQuery();
      if (rs.next()) {
        event =
            new Event(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("person_id"),
                rs.getFloat("latitude"),
                rs.getFloat("longitude"),
                rs.getString("country"),
                rs.getString("city"),
                rs.getString("event_type"),
                rs.getInt("year"));
        return event;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding event");
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
   * Attempt to find events with given username in database
   *
   * @param username
   * @return matching events
   * @throws DataAccessException
   */
  public List<Event> findEventsForUser(String username) throws DataAccessException {
    List<Event> events = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM Events WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      while (rs.next()) {
        Event person =
            new Event(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("person_id"),
                rs.getFloat("latitude"),
                rs.getFloat("longitude"),
                rs.getString("country"),
                rs.getString("city"),
                rs.getString("event_type"),
                rs.getInt("year"));
        events.add(person);
      }
      return events;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding events");
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
