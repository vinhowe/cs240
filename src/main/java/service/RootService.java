package service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadDataRequest;
import response.BaseResponse;
import response.MessageResponse;

import java.util.UUID;

public class RootService {
  private final UserDAO userDAO;
  private final PersonDAO personDAO;
  private final EventDAO eventDAO;
  private final AuthTokenDAO authTokenDAO;

  public RootService(
      UserDAO userDAO, PersonDAO personDAO, EventDAO eventDAO, AuthTokenDAO authTokenDAO) {
    this.userDAO = userDAO;
    this.personDAO = personDAO;
    this.eventDAO = eventDAO;
    this.authTokenDAO = authTokenDAO;
  }

  /**
   * Clear entire database, erasing all data.
   *
   * @return A message response object indicating success or failure
   */
  public BaseResponse clearDatabase() {
    try {
      clearDatabaseImpl();
      return new MessageResponse(true, "Clear succeeded.");
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to clear database");
    }
  }

  /**
   * Clear all data in database and replace it with data specified in `request` parameter
   *
   * @param request
   * @return A response object containing statistics about numbers of objects added
   */
  public BaseResponse loadData(LoadDataRequest request) {
    try {
      clearDatabaseImpl();
      for (User user : request.getUsers()) {
        this.userDAO.insert(user);
      }
      for (Person person : request.getPersons()) {
        this.personDAO.insert(person);
      }
      for (Event event : request.getEvents()) {
        this.eventDAO.insert(event);
      }
      return new MessageResponse(
          true,
          String.format(
              "Successfully added %d users, %d persons, and %d events to the database.",
              request.getUsers().size(), request.getPersons().size(), request.getEvents().size()));
    } catch (DataAccessException e) {
      return new MessageResponse(
          false, "Error: Data access error while trying to load data into database");
    }
  }

  private void clearDatabaseImpl() throws DataAccessException {
    this.userDAO.clear();
    this.eventDAO.clear();
    this.personDAO.clear();
    this.authTokenDAO.clear();
  }
}
