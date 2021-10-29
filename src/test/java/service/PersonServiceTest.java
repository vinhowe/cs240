package service;

import dao.*;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.UserLoginRequest;
import response.*;
import service.data.DummyPersonData;

import java.sql.Connection;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
  private Database db;
  private UserDAO userDAO;
  private PersonDAO personDAO;
  private UserService userService;
  private PersonService personService;
  private Person bestPerson;
  private User bestUser;

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
    EventDAO eventDAO = new EventDAO(conn);
    bestPerson =
        new Person(
            "PERSON_ID",
            "ally",
            "Porsche",
            "",
            'F',
            "EXAMPLE_FATHER_ID",
            "EXAMPLE_MOTHER_ID",
            "EXAMPLE_SPOUSE_ID");
    bestUser =
        new User(
            "ally", "password", "alice@gmail.com", "Alicia", "Carnival", 'f', "Alice_Carnival");
    personService = new PersonService(personDAO, authTokenDAO);
    DummyPersonData dummyData =
        new DummyPersonData(
            Arrays.asList("Richard", "Dick", "Ricky"),
            Arrays.asList("Belle", "Ella", "Kelly"),
            Arrays.asList("Smith", "Jones", "Paul"),
            Arrays.asList(
                new DummyPersonData.Location("Canada", "Frobo", 1f, 2f),
                new DummyPersonData.Location("Brob", "Brf", -50000f, 20f)));
    userService = new UserService(userDAO, personDAO, eventDAO, authTokenDAO, dummyData);
  }

  @AfterEach
  public void tearDown() throws DataAccessException {
    db.closeConnection(false);
  }

  @Test
  public void getPersonPass() throws DataAccessException {
    userDAO.insert(bestUser);
    personDAO.insert(bestPerson);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(bestUser.getUsername(), bestUser.getPassword()));
    BaseResponse response = personService.getPerson(bestPerson.getID(), auth.getAuthToken());
    assertEquals(PersonResponse.class, response.getClass());
    assertTrue(response.isSuccess());
    assertEquals(bestPerson, ((PersonResponse) response).toPerson());
  }

  @Test
  public void getPersonFail() throws DataAccessException {
    userDAO.insert(bestUser);
    personDAO.insert(bestPerson);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(bestUser.getUsername(), bestUser.getPassword()));
    BaseResponse response = personService.getPerson("nonexistent person ID", auth.getAuthToken());
    assertEquals(MessageResponse.class, response.getClass());
    assertFalse(response.isSuccess());
    assertEquals(
        "Error: Person with given ID does not exist or you do not have permission to view them",
        ((MessageResponse) response).getMessage());
  }

  @Test
  public void getPersonsForUserPass() throws DataAccessException {
    userDAO.insert(bestUser);
    personDAO.insert(bestPerson);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(bestUser.getUsername(), bestUser.getPassword()));
    BaseResponse response = personService.getPersonsForCurrentUser(auth.getAuthToken());
    assertEquals(PersonsResponse.class, response.getClass());
    assertTrue(response.isSuccess());
    assertEquals(1, ((PersonsResponse) response).getPersonResponses().size());
    assertEquals(bestPerson, ((PersonsResponse) response).getPersonResponses().get(0).toPerson());
  }

  @Test
  public void getPersonsForUserOnlyShowAuthorizedPersons() throws DataAccessException {
    userDAO.insert(bestUser);
    personDAO.insert(bestPerson);
    User alternateUser = new User(bestUser);
    alternateUser.setUsername("alternate");
    userDAO.insert(alternateUser);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(alternateUser.getUsername(), alternateUser.getPassword()));
    BaseResponse response = personService.getPersonsForCurrentUser(auth.getAuthToken());
    assertEquals(PersonsResponse.class, response.getClass());
    assertTrue(response.isSuccess());
    assertEquals(0, ((PersonsResponse) response).getPersonResponses().size());
  }
}
