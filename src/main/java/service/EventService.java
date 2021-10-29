package service;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.EventDAO;
import model.Event;
import response.BaseResponse;
import response.EventResponse;
import response.EventsResponse;
import response.MessageResponse;

import java.util.stream.Collectors;

public class EventService {
  private final EventDAO eventDAO;
  private final AuthTokenDAO authTokenDAO;

  public EventService(EventDAO eventDAO, AuthTokenDAO authTokenDAO) {
    this.eventDAO = eventDAO;
    this.authTokenDAO = authTokenDAO;
  }

  /**
   * Get an event for the user with the given token
   *
   * @param eventID
   * @param token
   * @return An event response if successful
   */
  public BaseResponse getEvent(String eventID, String token) {
    try {
      ServiceUtil.TokenFetchResult result = ServiceUtil.attemptFetchAuthToken(token, authTokenDAO);
      if (result.hasError()) {
        return result.getErrorMessage();
      }
      Event event = eventDAO.find(eventID);
      if (event == null || !event.getUsername().equals(result.getToken().getUsername())) {
        return new MessageResponse(
            false,
            "Error: Event with given ID does not exist or you do not have permission to view it");
      }
      return EventResponse.fromEvent(event);
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to get person");
    }
  }

  /**
   * Get all events for the authenticated user
   *
   * @param token
   * @return A response containing a list of EventResponse objects
   */
  public BaseResponse getEventsForCurrentUser(String token) {
    try {
      ServiceUtil.TokenFetchResult result = ServiceUtil.attemptFetchAuthToken(token, authTokenDAO);
      if (result.hasError()) {
        return result.getErrorMessage();
      }

      return new EventsResponse(
          eventDAO.findEventsForUser(result.getToken().getUsername()).stream()
              .map(EventResponse::fromEvent)
              .collect(Collectors.toList()));
    } catch (DataAccessException e) {
      return new MessageResponse(false, "Error: Data access error while trying to get persons");
    }
  }
}
