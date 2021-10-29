package dao;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
  private Database db;
  private User bestUser;
  private UserDAO userDAO;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    bestUser = new User("abc", "def", "geh@gmail.com", "jkl", "mno", 'F', "QRST");
    Connection conn = db.getConnection();
    db.clearTable("Users");
    userDAO = new UserDAO(conn);
  }

  @AfterEach
  public void tearDown() throws DataAccessException {
    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {
    userDAO.insert(bestUser);
    User compareTest = userDAO.find(bestUser.getUsername());
    assertNotNull(compareTest);
    assertEquals(bestUser, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {
    userDAO.insert(bestUser);
    assertThrows(DataAccessException.class, () -> userDAO.insert(bestUser));
  }

  @Test
  public void findPass() throws DataAccessException {
    userDAO.insert(bestUser);
    User compareTest = userDAO.find(bestUser.getUsername());
    assertNotNull(compareTest);
    assertEquals(bestUser, compareTest);
  }

  @Test
  public void findFail() throws DataAccessException {
    userDAO.insert(bestUser);
    User compareTest = userDAO.find("A username that does not exist");
    assertNull(compareTest);
  }

  @Test
  public void clearPass() throws DataAccessException {
    userDAO.insert(bestUser);
    userDAO.clear();
    User compareTest = userDAO.find(bestUser.getUsername());
    assertNull(compareTest);
  }

  @Test
  public void clearForUserPass() throws DataAccessException {
    userDAO.insert(bestUser);
    userDAO.clearForUser(bestUser.getUsername());
    User compareTest = userDAO.find(bestUser.getUsername());
    assertNull(compareTest);
  }

  @Test
  public void clearForUserDoesNotAffectOtherUser() throws DataAccessException {
    userDAO.insert(bestUser);
    userDAO.clearForUser("alice");
    User compareTest = userDAO.find(bestUser.getUsername());
    assertNotNull(compareTest);
  }
}
