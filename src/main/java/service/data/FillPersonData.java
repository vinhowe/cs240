package service.data;

import model.Event;
import model.Person;

import java.util.ArrayList;
import java.util.List;

public final class FillPersonData {
  private final List<Person> persons;
  private final List<Event> events;

  public FillPersonData(List<Person> persons, List<Event> events) {
    this.persons = persons;
    this.events = events;
  }

  public FillPersonData() {
    this.persons = new ArrayList<>();
    this.events = new ArrayList<>();
  }

  public void addAll(FillPersonData fillData) {
    this.persons.addAll(fillData.getPersons());
    this.events.addAll(fillData.getEvents());
  }

  public List<Person> getPersons() {
    return persons;
  }

  public List<Event> getEvents() {
    return events;
  }
}
