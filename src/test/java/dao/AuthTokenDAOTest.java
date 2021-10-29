package dao;

import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
  private Database db;
  private AuthToken bestToken;
  private AuthTokenDAO authTokenDAO;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    bestToken = new AuthToken(UUID.randomUUID(), "alice");
    Connection conn = db.getConnection();
    db.clearTable("Tokens");
    authTokenDAO = new AuthTokenDAO(conn);
  }

  @AfterEach
  public void tearDown() throws DataAccessException {
    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {
    authTokenDAO.insert(bestToken);
    AuthToken compareTest = authTokenDAO.find(bestToken.getToken().toString());
    assertNotNull(compareTest);
    assertEquals(bestToken, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {
    authTokenDAO.insert(bestToken);
    assertThrows(DataAccessException.class, () -> authTokenDAO.insert(bestToken));
  }

  @Test
  public void findPass() throws DataAccessException {
    authTokenDAO.insert(bestToken);
    AuthToken compareTest = authTokenDAO.find(bestToken.getToken().toString());
    assertNotNull(compareTest);
    assertEquals(bestToken, compareTest);
  }

  @Test
  public void findFail() throws DataAccessException {
    authTokenDAO.insert(bestToken);
    AuthToken compareTest = authTokenDAO.find("nonexistent auto token value");
    assertNull(compareTest);
  }

  @Test
  public void clearPass() throws DataAccessException {
    authTokenDAO.insert(bestToken);
    authTokenDAO.clear();
    AuthToken compareTest = authTokenDAO.find(bestToken.getToken().toString());
    assertNull(compareTest);
  }
}
