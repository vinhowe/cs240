package response;

import java.util.List;

public class EventsResponse extends BaseResponse {
  private List<EventResponse> data;

  public EventsResponse(List<EventResponse> eventResponses) {
    super(true);
    this.data = eventResponses;
  }

  /** Get the event responses */
  public List<EventResponse> getEventResponses() {
    return data;
  }

  /** Set the event responses */
  public void setEventResponses(List<EventResponse> eventResponses) {
    this.data = eventResponses;
  }
}
