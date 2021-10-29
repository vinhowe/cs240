package response;

import java.util.List;

public class PersonsResponse extends BaseResponse {
  private List<PersonResponse> data;

  public PersonsResponse(List<PersonResponse> personResponses) {
    super(true);
    this.data = personResponses;
  }

  /** Get the person responses */
  public List<PersonResponse> getPersonResponses() {
    return data;
  }

  /** Set the person responses */
  public void setPersonResponses(List<PersonResponse> personResponses) {
    this.data = personResponses;
  }
}
