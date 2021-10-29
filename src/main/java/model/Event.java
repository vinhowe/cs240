package model;

public class Event {
  private String eventID;
  private String associatedUsername;
  private String personID;
  private float latitude;
  private float longitude;
  private String country;
  private String city;
  private String eventType;
  private int year;

  public Event(
      String id,
      String username,
      String personID,
      float latitude,
      float longitude,
      String country,
      String city,
      String eventType,
      int year) {
    this.eventID = id;
    this.associatedUsername = username;
    this.personID = personID;
    this.latitude = latitude;
    this.longitude = longitude;
    this.country = country;
    this.city = city;
    this.eventType = eventType;
    this.year = year;
  }

  public Event(Event event) {
    this.eventID = event.eventID;
    this.associatedUsername = event.associatedUsername;
    this.personID = event.personID;
    this.latitude = event.latitude;
    this.longitude = event.longitude;
    this.country = event.country;
    this.city = event.city;
    this.eventType = event.eventType;
    this.year = event.year;
  }

  /** Get the Event ID */
  public String getID() {
    return eventID;
  }

  /** Set the Event ID */
  public void setID(String id) {
    this.eventID = id;
  }

  /** Get the user's name */
  public String getUsername() {
    return associatedUsername;
  }

  /** Set the user's name */
  public void setUsername(String username) {
    this.associatedUsername = username;
  }

  /** Get the Person's ID */
  public String getPersonID() {
    return personID;
  }

  /** Set the Person's ID */
  public void setPersonID(String personID) {
    this.personID = personID;
  }

  /** Get the Latitude */
  public float getLatitude() {
    return latitude;
  }

  /** Set the Latitude */
  public void setLatitude(float latitude) {
    this.latitude = latitude;
  }

  /** Get the Longitude */
  public float getLongitude() {
    return longitude;
  }

  /** Set the Longitude */
  public void setLongitude(float longitude) {
    this.longitude = longitude;
  }

  /** Get the Country */
  public String getCountry() {
    return country;
  }

  /** Set the Country */
  public void setCountry(String country) {
    this.country = country;
  }

  /** Get the City */
  public String getCity() {
    return city;
  }

  /** Set the City */
  public void setCity(String city) {
    this.city = city;
  }

  /** Get Event Type */
  public String getEventType() {
    return eventType;
  }

  /** Set the Event Type */
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  /** Get the Year */
  public int getYear() {
    return year;
  }

  /** Set the Year */
  public void setYear(int year) {
    this.year = year;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o instanceof Event) {
      Event oEvent = (Event) o;
      return oEvent.getID().equals(getID())
          && oEvent.getUsername().equals(getUsername())
          && oEvent.getPersonID().equals(getPersonID())
          && oEvent.getLatitude() == (getLatitude())
          && oEvent.getLongitude() == (getLongitude())
          && oEvent.getCountry().equals(getCountry())
          && oEvent.getCity().equals(getCity())
          && oEvent.getEventType().equals(getEventType())
          && oEvent.getYear() == (getYear());
    } else {
      return false;
    }
  }
}
