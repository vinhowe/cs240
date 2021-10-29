package response;

import model.Person;

public class PersonResponse extends BaseResponse {
  private String associatedUsername;
  private String personID;
  private String firstName;
  private String lastName;
  private char gender;
  private String fatherID;
  private String motherID;
  private String spouseID;

  public PersonResponse(
      String associatedUsername,
      String personID,
      String firstName,
      String lastName,
      char gender,
      String fatherID,
      String motherID,
      String spouseID) {
    super(true);
    this.associatedUsername = associatedUsername;
    this.personID = personID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.fatherID = fatherID;
    this.motherID = motherID;
    this.spouseID = spouseID;
  }

  public static PersonResponse fromPerson(Person person) {
    return new PersonResponse(
        person.getUsername(),
        person.getID(),
        person.getFirstName(),
        person.getLastName(),
        person.getGender(),
        person.getFatherID(),
        person.getMotherID(),
        person.getSpouseID());
  }

  public Person toPerson() {
    return new Person(
        personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
  }

  /** Get the associated username */
  public String getAssociatedUsername() {
    return associatedUsername;
  }

  /** Set the associated username */
  public void setAssociatedUsername(String associatedUsername) {
    this.associatedUsername = associatedUsername;
  }

  /** Get the person ID */
  public String getPersonID() {
    return personID;
  }

  /** Set the person ID */
  public void setPersonID(String personID) {
    this.personID = personID;
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

  /** Get the father's ID */
  public String getFatherID() {
    return fatherID;
  }

  /** Set the father's ID */
  public void setFatherID(String fatherID) {
    this.fatherID = fatherID;
  }

  /** Get the mother's ID */
  public String getMotherID() {
    return motherID;
  }

  /** Set the mother's ID */
  public void setMotherID(String motherID) {
    this.motherID = motherID;
  }

  /** Get the spouse's ID */
  public String getSpouseID() {
    return spouseID;
  }

  /** Set the spouse's ID */
  public void setSpouseID(String spouseID) {
    this.spouseID = spouseID;
  }
}
