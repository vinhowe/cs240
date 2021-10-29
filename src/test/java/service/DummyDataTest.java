package service;

import dao.DataAccessException;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.data.DummyPersonData;
import service.data.FillPersonData;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DummyDataTest {
  private DummyPersonData dummyData;
  private Person rootChild;

  @BeforeEach
  public void setUp() throws DataAccessException {
    dummyData =
        new DummyPersonData(
            Arrays.asList("Richard", "Dick", "Ricky"),
            Arrays.asList("Belle", "Ella", "Kelly"),
            Arrays.asList("Smith", "Jones", "Paul"),
            Arrays.asList(
                new DummyPersonData.Location("Canada", "Frobo", 1f, 2f),
                new DummyPersonData.Location("Brob", "Brf", -50000f, 2f)));
    rootChild = new Person("abc-xyz", "user-name", "Abba", "baab", 'm', null, null, null);
  }

  @Test
  public void createDummyData() {
    FillPersonData people = dummyData.generateFakePeople(rootChild, 4, 2000);
    assertEquals(30, people.getPersons().size());
    assertEquals(90, people.getEvents().size());
  }
}
