package model;

import java.util.Objects;

public class User {
  private String username;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private char gender;
  private String personID;

  public User(
      String username,
      String password,
      String email,
      String firstName,
      String lastName,
      char gender,
      String personID) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.personID = personID;
  }

  public User(User other) {
    this.username = other.getUsername();
    this.password = other.getPassword();
    this.email = other.getEmail();
    this.firstName = other.getFirstName();
    this.lastName = other.getLastName();
    this.gender = other.getGender();
    this.personID = other.getPersonID();
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

  /** Get the associated person ID */
  public String getPersonID() {
    return personID;
  }

  /** Set the associated person ID */
  public void setPersonID(String personID) {
    this.personID = personID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return gender == user.gender
        && Objects.equals(username, user.username)
        && Objects.equals(password, user.password)
        && Objects.equals(email, user.email)
        && Objects.equals(firstName, user.firstName)
        && Objects.equals(lastName, user.lastName)
        && Objects.equals(personID, user.personID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email, firstName, lastName, gender, personID);
  }
}
