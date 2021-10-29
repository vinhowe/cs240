package dao;

import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
  private Database db;
  private Event bestEvent;
  private User bestUser;
  private EventDAO eventDAO;
  private UserDAO userDAO;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    bestEvent =
        new Event(
            "Biking_123A",
            "abc",
            "Gale123A",
            35.9f,
            140.1f,
            "Japan",
            "Ushiku",
            "Biking_Around",
            2016);
    bestUser = new User("abc", "def", "geh@gmail.com", "jkl", "mno", 'F', "QRST");
    Connection conn = db.getConnection();
    db.clearTable("Events");
    eventDAO = new EventDAO(conn);
    userDAO = new UserDAO(conn);
  }

  @AfterEach
  public void tearDown() throws DataAccessException {
    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {
    eventDAO.insert(bestEvent);
    Event compareTest = eventDAO.find(bestEvent.getID());
    assertNotNull(compareTest);
    assertEquals(bestEvent, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {
    eventDAO.insert(bestEvent);
    assertThrows(DataAccessException.class, () -> eventDAO.insert(bestEvent));
  }

  @Test
  public void findPass() throws DataAccessException {
    eventDAO.insert(bestEvent);
    Event compareTest = eventDAO.find(bestEvent.getID());
    assertNotNull(compareTest);
    assertEquals(bestEvent, compareTest);
  }

  @Test
  public void findFail() throws DataAccessException {
    eventDAO.insert(bestEvent);
    Event compareTest = eventDAO.find("An ID that does not exist");
    assertNull(compareTest);
  }

  @Test
  public void findEventsForUserPass() throws DataAccessException {
    userDAO.insert(bestUser);
    eventDAO.insert(bestEvent);
    List<Event> compareTest = eventDAO.findEventsForUser(bestUser.getUsername());
    assertNotNull(compareTest);
    assertEquals(bestEvent, compareTest.get(0));
  }

  @Test
  public void findEventsForUserFail() throws DataAccessException {
    userDAO.insert(bestUser);
    eventDAO.insert(bestEvent);
    List<Event> compareTest = eventDAO.findEventsForUser("A username that does not exist");
    assertEquals(0, compareTest.size());
  }

  @Test
  public void clearPass() throws DataAccessException {
    eventDAO.insert(bestEvent);
    eventDAO.clear();
    Event compareTest = eventDAO.find(bestEvent.getID());
    assertNull(compareTest);
  }

  @Test
  public void clearForUserPass() throws DataAccessException {
    eventDAO.insert(bestEvent);
    eventDAO.clearForUser(bestEvent.getUsername());
    Event compareTest = eventDAO.find(bestEvent.getID());
    assertNull(compareTest);
  }

  @Test
  public void clearForUserDoesNotAffectOtherUser() throws DataAccessException {
    eventDAO.insert(bestEvent);
    eventDAO.clearForUser("alice");
    Event compareTest = eventDAO.find(bestEvent.getID());
    assertNotNull(compareTest);
  }
}
