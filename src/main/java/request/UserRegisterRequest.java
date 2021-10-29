package request;

public class UserRegisterRequest {
  private String username;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private char gender;

  public UserRegisterRequest(
      String username,
      String password,
      String email,
      String firstName,
      String lastName,
      char gender) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
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

  /** Get the email */
  public String getEmail() {
    return email;
  }

  /** Set the email */
  public void setEmail(String email) {
    this.email = email;
  }

  /** Get the first name */
  public String getFirstName() {
    return firstName;
  }

  /** Set the first name */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /** Get the last name */
  public String getLastName() {
    return lastName;
  }

  /** Set the last name */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /** Get the gender */
  public char getGender() {
    return gender;
  }

  /** Set the gender */
  public void setGender(char gender) {
    this.gender = gender;
  }
}
