package service;

import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.UserLoginRequest;
import request.UserRegisterRequest;
import response.BaseResponse;
import response.MessageResponse;
import response.UserAuthResponse;
import service.data.DummyPersonData;

import java.sql.Connection;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
  private Database db;
  private UserService userService;
  private User bestUser;
  private UserRegisterRequest bestRegisterRequest;
  private UserLoginRequest bestLoginRequest;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    Connection conn = db.getConnection();
    db.clearTable("Users");
    db.clearTable("Persons");
    db.clearTable("Events");
    db.clearTable("Tokens");
    UserDAO userDAO = new UserDAO(conn);
    PersonDAO personDAO = new PersonDAO(conn);
    EventDAO eventDAO = new EventDAO(conn);
    AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
    bestUser =
        new User(
            "ally", "password", "alice@gmail.com", "Alicia", "Carnival", 'f', "Alice_Carnival");
    bestRegisterRequest =
        new UserRegisterRequest(
            bestUser.getUsername(),
            bestUser.getPassword(),
            bestUser.getEmail(),
            bestUser.getFirstName(),
            bestUser.getLastName(),
            bestUser.getGender());
    bestLoginRequest = new UserLoginRequest(bestUser.getUsername(), bestUser.getPassword());
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
  public void registerUserPass() {
    UserAuthResponse correctResponse = new UserAuthResponse(null, bestUser.getUsername(), null);
    BaseResponse response = userService.registerUser(bestRegisterRequest);
    assertEquals(correctResponse.getClass(), response.getClass());
    assertEquals(correctResponse.isSuccess(), response.isSuccess());
    assertEquals(correctResponse.getUsername(), ((UserAuthResponse) response).getUsername());
    assertNotNull(((UserAuthResponse) response).getAuthToken());
  }

  @Test
  public void registerUserFailIfExists() {
    userService.registerUser(bestRegisterRequest);
    BaseResponse response = userService.registerUser(bestRegisterRequest);
    assertEquals(MessageResponse.class, response.getClass());
    assertFalse(response.isSuccess());
  }

  @Test
  public void loginUser() {
    UserAuthResponse correctResponse = new UserAuthResponse(null, bestUser.getUsername(), null);
    userService.registerUser(bestRegisterRequest);
    BaseResponse response = userService.loginUser(bestLoginRequest);
    assertEquals(correctResponse.getClass(), response.getClass());
    assertEquals(correctResponse.isSuccess(), response.isSuccess());
    assertEquals(correctResponse.getUsername(), ((UserAuthResponse) response).getUsername());
    assertNotNull(((UserAuthResponse) response).getAuthToken());
  }

  @Test
  public void loginUserFailIfInvalidCredentials() {
    userService.registerUser(bestRegisterRequest);

    BaseResponse wrongUsernameResponse =
        userService.loginUser(new UserLoginRequest("alice", "password"));
    assertEquals(MessageResponse.class, wrongUsernameResponse.getClass());
    assertFalse(wrongUsernameResponse.isSuccess());

    BaseResponse wrongPasswordResponse =
        userService.loginUser(new UserLoginRequest("ally", "wrong-password"));
    assertEquals(MessageResponse.class, wrongPasswordResponse.getClass());
    assertFalse(wrongPasswordResponse.isSuccess());
  }

  @Test
  public void fillUserPass() {
    userService.registerUser(bestRegisterRequest);
    MessageResponse correctResponse =
        new MessageResponse(true, "Successfully added 15 persons and 43 events to the database.");
    BaseResponse response = userService.fillUser(bestUser.getUsername(), 3);
    assertEquals(correctResponse.getClass(), response.getClass());
    assertEquals(correctResponse.isSuccess(), response.isSuccess());
    assertEquals(correctResponse.getMessage(), ((MessageResponse) response).getMessage());
  }

  @Test
  public void fillUserInvalidUsernameFail() {
    userService.registerUser(bestRegisterRequest);
    BaseResponse response = userService.fillUser("alice", 3);
    assertEquals(MessageResponse.class, response.getClass());
    assertFalse(response.isSuccess());
  }
}
