package service.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import model.Person;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DummyPersonData {
  private final List<String> maleNames;
  private final List<String> femaleNames;
  private final List<String> surnames;
  private final List<Location> locations;
  private final Random random;

  public DummyPersonData(
      List<String> maleNames,
      List<String> femaleNames,
      List<String> surnames,
      List<Location> locations) {
    this.maleNames = maleNames;
    this.femaleNames = femaleNames;
    this.surnames = surnames;
    this.locations = locations;
    random = new Random();
  }

  public static DummyPersonData fromPaths(
      Path maleNamesPath, Path femaleNamesPath, Path surnamesPath, Path locationsPath, Gson gson)
      throws IOException {
    Type stringDummyType = new TypeToken<DummyDataContainer<String>>() {}.getType();
    Type locationDummyType = new TypeToken<DummyDataContainer<Location>>() {}.getType();

    DummyDataContainer<String> maleNames =
        gson.fromJson(Files.newBufferedReader(maleNamesPath), stringDummyType);
    DummyDataContainer<String> femaleNames =
        gson.fromJson(Files.newBufferedReader(femaleNamesPath), stringDummyType);
    DummyDataContainer<String> surnames =
        gson.fromJson(Files.newBufferedReader(surnamesPath), stringDummyType);
    DummyDataContainer<Location> locations =
        gson.fromJson(Files.newBufferedReader(locationsPath), locationDummyType);
    return new DummyPersonData(
        maleNames.getData(), femaleNames.getData(), surnames.getData(), locations.getData());
  }

  public List<String> getMaleNames() {
    return maleNames;
  }

  public List<String> getFemaleNames() {
    return femaleNames;
  }

  public List<String> getSurnames() {
    return surnames;
  }

  public List<Location> getLocations() {
    return locations;
  }

  private static <T> T randomFromList(List<T> items) {
    return items.get(new Random().nextInt(items.size()));
  }

  /**
   * Generates a fake person
   *
   * @param fatherID
   * @param motherID
   * @param spouseID
   * @return
   */
  public Person generateFakePerson(
      String username, String fatherID, String motherID, String spouseID, char gender) {
    String name = randomFromList(gender == 'f' ? femaleNames : maleNames);
    return new Person(
        UUID.randomUUID().toString(),
        username,
        name,
        randomFromList(surnames),
        gender,
        fatherID,
        motherID,
        spouseID);
  }

  public Event generateBirthEvent(Person person, int baseYear) {
    Location birthLocation = randomFromList(locations);
    return new Event(
        UUID.randomUUID().toString(),
        person.getUsername(),
        person.getID(),
        birthLocation.getLatitude(),
        birthLocation.getLongitude(),
        birthLocation.getCountry(),
        birthLocation.getCity(),
        "birth",
        baseYear - 2 + random.nextInt(4));
  }

  public Event generateMarriageEvent(Person person, int baseYear) {
    Location marriageLocation = randomFromList(locations);
    return new Event(
        UUID.randomUUID().toString(),
        person.getUsername(),
        person.getID(),
        marriageLocation.getLatitude(),
        marriageLocation.getLongitude(),
        marriageLocation.getCountry(),
        marriageLocation.getCity(),
        "marriage",
        baseYear + 20 + random.nextInt(4));
  }

  public Event generateDeathEvent(Person person, int baseYear) {
    Location deathLocation = randomFromList(locations);
    return new Event(
        UUID.randomUUID().toString(),
        person.getUsername(),
        person.getID(),
        deathLocation.getLatitude(),
        deathLocation.getLongitude(),
        deathLocation.getCountry(),
        deathLocation.getCity(),
        "death",
        baseYear + 65 + random.nextInt(30));
  }

  public List<Event> generateFakeEventsForCouple(Person woman, Person man, int baseYear) {
    Event womanMarriageEvent = generateMarriageEvent(woman, baseYear);
    Event manMarriageEvent = new Event(womanMarriageEvent);
    manMarriageEvent.setID(UUID.randomUUID().toString());
    manMarriageEvent.setPersonID(man.getID());
    return List.of(
        generateBirthEvent(woman, baseYear),
        generateBirthEvent(man, baseYear),
        womanMarriageEvent,
        manMarriageEvent,
        generateDeathEvent(woman, baseYear),
        generateDeathEvent(man, baseYear));
  }

  public FillPersonData generateFakePeople(Person child, int generations, int baseYear) {
    FillPersonData data = new FillPersonData();
    Person father = generateFakePerson(child.getUsername(), null, null, null, 'm');
    Person mother = generateFakePerson(child.getUsername(), null, null, father.getID(), 'f');
    father.setSpouseID(mother.getID());
    child.setFatherID(father.getID());
    child.setMotherID(mother.getID());
    data.getPersons().add(father);
    data.getPersons().add(mother);
    data.getEvents().addAll(generateFakeEventsForCouple(mother, father, baseYear - 30));
    if (generations > 1) {
      data.addAll(generateFakePeople(father, generations - 1, baseYear - 30));
      data.addAll(generateFakePeople(mother, generations - 1, baseYear - 30));
    }
    return data;
  }

  public static class Location {
    private final String country;
    private final String city;
    private final float latitude;
    private final float longitude;

    public Location(String country, String city, float latitude, float longitude) {
      this.country = country;
      this.city = city;
      this.latitude = latitude;
      this.longitude = longitude;
    }

    public String getCountry() {
      return country;
    }

    public String getCity() {
      return city;
    }

    public float getLatitude() {
      return latitude;
    }

    public float getLongitude() {
      return longitude;
    }
  }
}

class DummyDataContainer<T> {
  private final List<T> data;

  public DummyDataContainer(List<T> data) {
    this.data = data;
  }

  public List<T> getData() {
    return data;
  }
}
