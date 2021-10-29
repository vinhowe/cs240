package dao;

import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
  private Database db;
  private Person bestPerson;
  private User bestUser;
  private PersonDAO personDAO;
  private UserDAO userDAO;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    bestPerson =
        new Person(
            "PERSON_ID",
            "abc",
            "Porsche",
            "",
            'F',
            "EXAMPLE_FATHER_ID",
            "EXAMPLE_MOTHER_ID",
            "EXAMPLE_SPOUSE_ID");
    bestUser = new User("abc", "def", "geh@gmail.com", "jkl", "mno", 'F', "QRST");
    Connection conn = db.getConnection();
    db.clearTable("Persons");
    personDAO = new PersonDAO(conn);
    userDAO = new UserDAO(conn);
  }

  @AfterEach
  public void tearDown() throws DataAccessException {
    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {
    personDAO.insert(bestPerson);
    Person compareTest = personDAO.find(bestPerson.getID());
    assertNotNull(compareTest);
    assertEquals(bestPerson, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {
    personDAO.insert(bestPerson);
    assertThrows(DataAccessException.class, () -> personDAO.insert(bestPerson));
  }

  @Test
  public void findPass() throws DataAccessException {
    personDAO.insert(bestPerson);
    Person compareTest = personDAO.find(bestPerson.getID());
    assertNotNull(compareTest);
    assertEquals(bestPerson, compareTest);
  }

  @Test
  public void findFail() throws DataAccessException {
    personDAO.insert(bestPerson);
    Person compareTest = personDAO.find("An ID that does not exist");
    assertNull(compareTest);
  }

  @Test
  public void findPersonsForUserPass() throws DataAccessException {
    userDAO.insert(bestUser);
    personDAO.insert(bestPerson);
    List<Person> compareTest = personDAO.findPersonsForUser(bestUser.getUsername());
    assertNotNull(compareTest);
    assertEquals(bestPerson, compareTest.get(0));
  }

  @Test
  public void findPersonsForUserFail() throws DataAccessException {
    userDAO.insert(bestUser);
    personDAO.insert(bestPerson);
    List<Person> compareTest = personDAO.findPersonsForUser("A username that does not exist");
    assertEquals(0, compareTest.size());
  }

  @Test
  public void clearPass() throws DataAccessException {
    personDAO.insert(bestPerson);
    personDAO.clear();
    Person compareTest = personDAO.find(bestPerson.getID());
    assertNull(compareTest);
  }

  @Test
  public void clearForUserPass() throws DataAccessException {
    personDAO.insert(bestPerson);
    personDAO.clearForUser(bestPerson.getUsername());
    Person compareTest = personDAO.find(bestPerson.getID());
    assertNull(compareTest);
  }

  @Test
  public void clearForUserDoesNotAffectOtherUser() throws DataAccessException {
    personDAO.insert(bestPerson);
    personDAO.clearForUser("alice");
    Person compareTest = personDAO.find(bestPerson.getID());
    assertNotNull(compareTest);
  }
}
