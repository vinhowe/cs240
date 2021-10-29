package service;

import dao.*;
import model.Person;
import response.BaseResponse;
import response.MessageResponse;
import response.PersonResponse;
import response.PersonsResponse;

import java.util.stream.Collectors;

public class PersonService {
  private final PersonDAO personDAO;
  private final AuthTokenDAO authTokenDAO;

  public PersonService(PersonDAO personDAO, AuthTokenDAO authTokenDAO) {
    this.personDAO = personDAO;
    this.authTokenDAO = authTokenDAO;
  }

  /**
   * Get a person for the authenticated user
   *
   * @param personID
   * @param token
   * @return A response containing fields from the Person model
   */
  public BaseResponse getPerson(String personID, String token) {
    try {
      ServiceUtil.TokenFetchResult result = ServiceUtil.attemptFetchAuthToken(token, authTokenDAO);
      if (result.hasError()) {
        return result.getErrorMessage();
      }
      Person person = personDAO.find(personID);
      if (person == null || !person.getUsername().equals(result.getToken().getUsername())) {
        return new MessageResponse(
            false,
            "Error: Person with given ID does not exist or you do not have permission to view them");
      }
      return PersonResponse.fromPerson(person);
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to get person");
    }
  }

  /**
   * Get all people visible to the authenticated user
   *
   * @param token
   * @return A response containing a list of PersonResponse objects
   */
  public BaseResponse getPersonsForCurrentUser(String token) {
    try {
      ServiceUtil.TokenFetchResult result = ServiceUtil.attemptFetchAuthToken(token, authTokenDAO);
      if (result.hasError()) {
        return result.getErrorMessage();
      }

      return new PersonsResponse(
          personDAO.findPersonsForUser(result.getToken().getUsername()).stream()
              .map(PersonResponse::fromPerson)
              .collect(Collectors.toList()));
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to get persons");
    }
  }
}
