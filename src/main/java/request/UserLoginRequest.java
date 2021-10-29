package request;

public class UserLoginRequest {
  private String username;
  private String password;

  public UserLoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /** Get the username */
  public String getUsername() {
    return username;
  }

  /** Set the username */
  public void setUsername(String username) {
    this.username = username;
  }

  /** Get the password */
  public String getPassword() {
    return password;
  }

  /** Set the password */
  public void setPassword(String password) {
    this.password = password;
  }
}
