package response;

import model.Event;

public class EventResponse extends BaseResponse {
  private String associatedUsername;
  private String eventID;
  private String personID;
  private float latitude;
  private float longitude;
  private String country;
  private String city;
  private String eventType;
  private int year;

  public EventResponse(
      String associatedUsername,
      String eventID,
      String personID,
      float latitude,
      float longitude,
      String country,
      String city,
      String eventType,
      int year) {
    super(true);
    this.associatedUsername = associatedUsername;
    this.eventID = eventID;
    this.personID = personID;
    this.latitude = latitude;
    this.longitude = longitude;
    this.country = country;
    this.city = city;
    this.eventType = eventType;
    this.year = year;
  }

  public static EventResponse fromEvent(Event event) {
    return new EventResponse(
        event.getUsername(),
        event.getID(),
        event.getPersonID(),
        event.getLatitude(),
        event.getLongitude(),
        event.getCountry(),
        event.getCity(),
        event.getEventType(),
        event.getYear());
  }

  public Event toEvent() {
    return new Event(
        this.eventID,
        this.associatedUsername,
        this.personID,
        this.latitude,
        this.longitude,
        this.country,
        this.city,
        this.eventType,
        this.year);
  }

  /** Get the associated username */
  public String getAssociatedUsername() {
    return associatedUsername;
  }

  /** Set the associated username */
  public void setAssociatedUsername(String associatedUsername) {
    this.associatedUsername = associatedUsername;
  }

  /** Get the event ID */
  public String getEventID() {
    return eventID;
  }

  /** Set the event ID */
  public void setEventID(String eventID) {
    this.eventID = eventID;
  }

  /** Get the person ID */
  public String getPersonID() {
    return personID;
  }

  /** Set the person ID */
  public void setPersonID(String personID) {
    this.personID = personID;
  }

  /** Get the latitude */
  public double getLatitude() {
    return latitude;
  }

  /** Set the latitude */
  public void setLatitude(float latitude) {
    this.latitude = latitude;
  }

  /** Get the longitude */
  public double getLongitude() {
    return longitude;
  }

  /** Set the longitude */
  public void setLongitude(float longitude) {
    this.longitude = longitude;
  }

  /** Get the country */
  public String getCountry() {
    return country;
  }

  /** Set the country */
  public void setCountry(String country) {
    this.country = country;
  }

  /** Get the city */
  public String getCity() {
    return city;
  }

  /** Set the city */
  public void setCity(String city) {
    this.city = city;
  }

  /** Get the event type */
  public String getEventType() {
    return eventType;
  }

  /** Set the event type */
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  /** Get the year */
  public int getYear() {
    return year;
  }

  /** Set the year */
  public void setYear(int year) {
    this.year = year;
  }
}
