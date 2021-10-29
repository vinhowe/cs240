package request;

import model.Event;
import model.Person;
import model.User;

import java.util.List;

public class LoadDataRequest {
  private List<User> users;
  private List<Person> persons;
  private List<Event> events;

  public LoadDataRequest(List<User> users, List<Person> persons, List<Event> events) {
    this.users = users;
    this.persons = persons;
    this.events = events;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public List<Person> getPersons() {
    return persons;
  }

  public void setPersons(List<Person> persons) {
    this.persons = persons;
  }

  public List<Event> getEvents() {
    return events;
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }
}
