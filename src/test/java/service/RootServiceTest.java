package service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadDataRequest;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RootServiceTest {
  public static final List<User> USER_LIST =
      List.of(
          new User("ab", "bc", "cd", "de", "ef", 'm', "fg"),
          new User("bc", "cd", "de", "ef", "fg", 'm', "gh"),
          new User("cd", "de", "ef", "fg", "gh", 'm', "hi"));
  public static final List<Person> PERSON_LIST =
      List.of(
          new Person("ab", "bc", "cd", "de", 'm', "ef", "fg", "gh"),
          new Person("bc", "cd", "de", "ef", 'f', "fg", "gh", "hi"),
          new Person("cd", "de", "ef", "fg", 'm', "gh", "hi", "ij"));
  public static final List<Event> EVENT_LIST =
      List.of(
          new Event("ab", "bc", "cd", 123.456f, 789.012f, "abc", "cde", "birth", 2021),
          new Event("bc", "cd", "de", 223.456f, 889.012f, "bcd", "def", "death", 2022),
          new Event("cd", "de", "ef", 323.456f, 989.012f, "cde", "efg", "zombification", 2100));
  public static LoadDataRequest TEST_LOAD_DATA_REQUEST =
      new LoadDataRequest(USER_LIST, PERSON_LIST, EVENT_LIST);

  private Database db;
  private UserDAO userDAO;
  private PersonDAO personDAO;
  private EventDAO eventDAO;
  private RootService rootService;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    Connection conn = db.getConnection();
    db.clearTable("Users");
    db.clearTable("Persons");
    db.clearTable("Events");
    db.clearTable("Tokens");
    userDAO = new UserDAO(conn);
    personDAO = new PersonDAO(conn);
    AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
    eventDAO = new EventDAO(conn);
    rootService = new RootService(userDAO, personDAO, eventDAO, authTokenDAO);
  }

  @AfterEach
  public void tearDown() throws DataAccessException {
    db.closeConnection(false);
  }

  @Test
  public void testLoadData() throws DataAccessException {
    rootService.loadData(TEST_LOAD_DATA_REQUEST);
    for (User user : USER_LIST) {
      assertEquals(user, userDAO.find(user.getUsername()));
    }
    for (Person person : PERSON_LIST) {
      assertEquals(person, personDAO.find(person.getID()));
    }
    for (Event event : EVENT_LIST) {
      assertEquals(event, eventDAO.find(event.getID()));
    }
  }

  @Test
  public void testLoadDataRemovesExistingData() throws DataAccessException {
    User alternateUser = new User("fake username", "bc", "cd", "de", "ef", 'm', "fg");
    Person alternatePerson =
        new Person(
            "alternate person ID",
            "ally",
            "Porsche",
            "",
            'F',
            "EXAMPLE_FATHER_ID",
            "EXAMPLE_MOTHER_ID",
            "EXAMPLE_SPOUSE_ID");
    Event alternateEvent =
        new Event(
            "alternate event ID",
            "ally",
            "Gale123A",
            35.9f,
            140.1f,
            "Japan",
            "Ushiku",
            "Biking_Around",
            2016);
    rootService.loadData(TEST_LOAD_DATA_REQUEST);
    assertNull(userDAO.find(alternateUser.getUsername()));
    assertNull(personDAO.find(alternatePerson.getID()));
    assertNull(eventDAO.find(alternateEvent.getID()));
  }

  @Test
  public void clearRemovesData() throws DataAccessException {
    rootService.loadData(TEST_LOAD_DATA_REQUEST);
    rootService.clearDatabase();
    for (User user : USER_LIST) {
      assertNull(userDAO.find(user.getUsername()));
    }
    for (Person person : PERSON_LIST) {
      assertNull(personDAO.find(person.getID()));
    }
    for (Event event : EVENT_LIST) {
      assertNull(eventDAO.find(event.getID()));
    }
  }

  @Test
  public void dataCorrectAfterClear() throws DataAccessException {
    rootService.loadData(TEST_LOAD_DATA_REQUEST);
    rootService.clearDatabase();
    rootService.loadData(TEST_LOAD_DATA_REQUEST);
    for (User user : USER_LIST) {
      assertEquals(user, userDAO.find(user.getUsername()));
    }
    for (Person person : PERSON_LIST) {
      assertEquals(person, personDAO.find(person.getID()));
    }
    for (Event event : EVENT_LIST) {
      assertEquals(event, eventDAO.find(event.getID()));
    }
  }
}
