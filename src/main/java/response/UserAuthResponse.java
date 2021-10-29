package response;

public class UserAuthResponse extends BaseResponse {
  private String authtoken;
  private String username;
  private String personID;

  public UserAuthResponse(String authToken, String username, String personID) {
    super(true);
    this.authtoken = authToken;
    this.username = username;
    this.personID = personID;
  }

  /** Get the auth token */
  public String getAuthToken() {
    return authtoken;
  }

  /** Set the auth token */
  public void setAuthToken(String authToken) {
    this.authtoken = authToken;
  }

  /** Get the username */
  public String getUsername() {
    return username;
  }

  /** Set the username */
  public void setUsername(String username) {
    this.username = username;
  }

  /** Get the person ID */
  public String getPersonID() {
    return personID;
  }

  /** Set the person ID */
  public void setPersonID(String personID) {
    this.personID = personID;
  }
}
