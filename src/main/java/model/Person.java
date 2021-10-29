package model;

import java.util.Objects;

public class Person {
  private String personID;
  private String associatedUsername;
  private String firstName;
  private String lastName;
  private char gender;
  private String fatherID;
  private String motherID;
  private String spouseID;

  public Person(
      String id,
      String username,
      String firstName,
      String lastName,
      char gender,
      String fatherID,
      String motherID,
      String spouseID) {
    this.personID = id;
    this.associatedUsername = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.fatherID = fatherID;
    this.motherID = motherID;
    this.spouseID = spouseID;
  }

  /** Get this person's ID */
  public String getID() {
    return personID;
  }

  /** Set this person's ID */
  public void setID(String id) {
    this.personID = id;
  }

  /** Get the associated username */
  public String getUsername() {
    return associatedUsername;
  }

  /** Set the associated username */
  public void setUsername(String username) {
    this.associatedUsername = username;
  }

  /** Get this person's first name */
  public String getFirstName() {
    return firstName;
  }

  /** Set this person's first name */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /** Get this person's last name */
  public String getLastName() {
    return lastName;
  }

  /** Set this person's last name */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /** Set this person's gender */
  public char getGender() {
    return gender;
  }

  /** Set this person's gender */
  public void setGender(char gender) {
    this.gender = gender;
  }

  /** Get this person's father's person ID */
  public String getFatherID() {
    return fatherID;
  }

  /** Set this person's father's person ID */
  public void setFatherID(String fatherID) {
    this.fatherID = fatherID;
  }

  /** Get this person's father's person ID */
  public String getMotherID() {
    return motherID;
  }

  /** Set this person's mother's person ID */
  public void setMotherID(String motherID) {
    this.motherID = motherID;
  }

  /** Get this person's spouse's person ID */
  public String getSpouseID() {
    return spouseID;
  }

  /** Set this person's spouse's person ID */
  public void setSpouseID(String spouseID) {
    this.spouseID = spouseID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return gender == person.gender
        && Objects.equals(personID, person.personID)
        && Objects.equals(associatedUsername, person.associatedUsername)
        && Objects.equals(firstName, person.firstName)
        && Objects.equals(lastName, person.lastName)
        && Objects.equals(fatherID, person.fatherID)
        && Objects.equals(motherID, person.motherID)
        && Objects.equals(spouseID, person.spouseID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
  }
}
