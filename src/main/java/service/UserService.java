package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import request.UserLoginRequest;
import request.UserRegisterRequest;
import response.BaseResponse;
import response.MessageResponse;
import response.UserAuthResponse;
import service.data.DummyPersonData;
import service.data.FillPersonData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
  private final UserDAO userDAO;
  private final PersonDAO personDAO;
  private final EventDAO eventDAO;
  private final AuthTokenDAO authTokenDAO;
  private final DummyPersonData dummyData;

  public UserService(
      UserDAO userDAO,
      PersonDAO personDAO,
      EventDAO eventDAO,
      AuthTokenDAO authTokenDAO,
      DummyPersonData dummyData) {
    this.userDAO = userDAO;
    this.personDAO = personDAO;
    this.eventDAO = eventDAO;
    this.authTokenDAO = authTokenDAO;
    this.dummyData = dummyData;
  }

  /**
   * Register a new user
   *
   * @param request
   * @return a response object containing details about the new user
   */
  public BaseResponse registerUser(UserRegisterRequest request) {
    try {
      AuthToken token = AuthToken.generate(request.getUsername());
      authTokenDAO.insert(token);
      User existingUser = userDAO.find(request.getUsername());
      if (existingUser != null) {
        return new MessageResponse(false, "Error: That username is taken");
      }
      User user =
          new User(
              request.getUsername(),
              request.getPassword(),
              request.getEmail(),
              request.getFirstName(),
              request.getLastName(),
              request.getGender(),
              null);
      FillData fillData = fillUserImpl(user, 4);
      return new UserAuthResponse(
          token.getToken().toString(), request.getUsername(), fillData.getPerson().getID());
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to register");
    }
  }

  /**
   * Log in an existing user
   *
   * @param request
   * @return a response object containing details about the new session
   */
  public BaseResponse loginUser(UserLoginRequest request) {

    try {
      User user = userDAO.find(request.getUsername());

      if (user == null) {
        return new MessageResponse(false, "Error: User does not exist");
      }

      if (!Objects.equals(user.getPassword(), request.getPassword())) {
        return new MessageResponse(false, "Error: Invalid password");
      }

      AuthToken token = AuthToken.generate(request.getUsername());
      authTokenDAO.insert(token);

      return new UserAuthResponse(
          token.getToken().toString(), request.getUsername(), user.getPersonID());
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to login");
    }
  }

  /**
   * Populate database with fake family history data for a given user
   *
   * @param username
   * @param generations
   */
  public BaseResponse fillUser(String username, int generations) {
    try {
      // We need to throw an error if the user doesn't exist, so we'll try to find them
      User user = userDAO.find(username);

      if (user == null) {
        return new MessageResponse(false, "Error: User does not exist");
      }

      FillData stats = fillUserImpl(user, generations);

      return new MessageResponse(
          true,
          String.format(
              "Successfully added %d persons and %d events to the database.",
              stats.getPersonCount(), stats.getEventCount()));
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to fill user data");
    }
  }

  private FillData fillUserImpl(User user, int generations) throws DataAccessException {
    Person userPerson =
        new Person(
            UUID.randomUUID().toString(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getGender(),
            null,
            null,
            null);
    Event birthEvent = dummyData.generateBirthEvent(userPerson, 2000);
    FillPersonData fillData = dummyData.generateFakePeople(userPerson, generations, 2000);

    personDAO.clearForUser(user.getUsername());
    eventDAO.clearForUser(user.getUsername());
    personDAO.insert(userPerson);
    eventDAO.insert(birthEvent);
    user.setPersonID(userPerson.getID());
    userDAO.clearForUser(user.getUsername());
    userDAO.insert(user);

    for (Person person : fillData.getPersons()) {
      personDAO.insert(person);
    }
    for (Event event : fillData.getEvents()) {
      eventDAO.insert(event);
    }

    return new FillData(
        fillData.getPersons().size() + 1, fillData.getEvents().size() + 1, userPerson);
  }
}

class FillData {
  private final int personCount;
  private final int eventCount;
  private final Person person;

  FillData(int personCount, int eventCount, Person person) {
    this.personCount = personCount;
    this.eventCount = eventCount;
    this.person = person;
  }

  public int getPersonCount() {
    return personCount;
  }

  public int getEventCount() {
    return eventCount;
  }

  public Person getPerson() {
    return person;
  }
}
