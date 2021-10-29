package service;

import dao.*;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.UserLoginRequest;
import request.UserRegisterRequest;
import response.*;
import service.data.DummyPersonData;

import java.sql.Connection;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
  private Database db;
  private EventDAO eventDAO;
  private UserDAO userDAO;
  private UserService userService;
  private EventService eventService;
  private Event bestEvent;
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
    PersonDAO personDAO = new PersonDAO(conn);
    eventDAO = new EventDAO(conn);
    AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
    bestEvent =
        new Event(
            "Biking_123A",
            "ally",
            "Gale123A",
            35.9f,
            140.1f,
            "Japan",
            "Ushiku",
            "Biking_Around",
            2016);
    bestUser =
        new User(
            "ally", "password", "alice@gmail.com", "Alicia", "Carnival", 'f', "Alice_Carnival");
    eventService = new EventService(eventDAO, authTokenDAO);
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
  public void getEventPass() throws DataAccessException {
    userDAO.insert(bestUser);
    eventDAO.insert(bestEvent);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(bestUser.getUsername(), bestUser.getPassword()));
    BaseResponse response = eventService.getEvent(bestEvent.getID(), auth.getAuthToken());
    assertEquals(EventResponse.class, response.getClass());
    assertTrue(response.isSuccess());
    assertEquals(bestEvent, ((EventResponse) response).toEvent());
  }

  @Test
  public void getEventFail() throws DataAccessException {
    userDAO.insert(bestUser);
    eventDAO.insert(bestEvent);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(bestUser.getUsername(), bestUser.getPassword()));
    BaseResponse response = eventService.getEvent("nonexistent event ID", auth.getAuthToken());
    assertEquals(MessageResponse.class, response.getClass());
    assertFalse(response.isSuccess());
    assertEquals(
        "Error: Event with given ID does not exist or you do not have permission to view it",
        ((MessageResponse) response).getMessage());
  }

  @Test
  public void getEventsForUserPass() throws DataAccessException {
    userDAO.insert(bestUser);
    eventDAO.insert(bestEvent);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(bestUser.getUsername(), bestUser.getPassword()));
    BaseResponse response = eventService.getEventsForCurrentUser(auth.getAuthToken());
    assertEquals(EventsResponse.class, response.getClass());
    assertTrue(response.isSuccess());
    assertEquals(1, ((EventsResponse) response).getEventResponses().size());
    assertEquals(bestEvent, ((EventsResponse) response).getEventResponses().get(0).toEvent());
  }

  @Test
  public void getEventsForUserOnlyShowAuthorizedEvents() throws DataAccessException {
    userDAO.insert(bestUser);
    eventDAO.insert(bestEvent);
    User alternateUser = new User(bestUser);
    alternateUser.setUsername("alternate");
    userDAO.insert(alternateUser);
    UserAuthResponse auth =
        (UserAuthResponse)
            userService.loginUser(
                new UserLoginRequest(alternateUser.getUsername(), alternateUser.getPassword()));
    BaseResponse response = eventService.getEventsForCurrentUser(auth.getAuthToken());
    assertEquals(EventsResponse.class, response.getClass());
    assertTrue(response.isSuccess());
    assertEquals(0, ((EventsResponse) response).getEventResponses().size());
  }
}
