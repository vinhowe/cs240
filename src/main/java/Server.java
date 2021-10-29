import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.sql.Connection;

import com.google.gson.Gson;
import dao.*;
import handler.*;

import com.sun.net.httpserver.*;
import request.LoadDataRequest;
import request.UserLoginRequest;
import request.UserRegisterRequest;
import service.*;
import service.data.DummyPersonData;

public class Server {

  private static final int MAX_WAITING_CONNECTIONS = 12;

  private final Gson gson;

  private final UserService userService;
  private final EventService eventService;
  private final PersonService personService;
  private final RootService rootService;

  public Server() throws DataAccessException, IOException {
    Database database = new Database();
    Connection connection = database.getConnection(true);
    gson = new Gson();

    DummyPersonData dummyPersonData =
        DummyPersonData.fromPaths(
            Path.of("json/mnames.json"),
            Path.of("json/fnames.json"),
            Path.of("json/snames.json"),
            Path.of("json/locations.json"),
            gson);

    UserDAO userDAO = new UserDAO(connection);
    EventDAO eventDAO = new EventDAO(connection);
    PersonDAO personDAO = new PersonDAO(connection);
    AuthTokenDAO authTokenDAO = new AuthTokenDAO(connection);

    userService = new UserService(userDAO, personDAO, eventDAO, authTokenDAO, dummyPersonData);
    eventService = new EventService(eventDAO, authTokenDAO);
    personService = new PersonService(personDAO, authTokenDAO);
    rootService = new RootService(userDAO, personDAO, eventDAO, authTokenDAO);
  }

  private void run(String portNumber) {

    System.out.println("Initializing Amazing Family Map Server");

    HttpServer server;
    try {
      server =
          HttpServer.create(
              new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    server.setExecutor(null);

    System.out.println("Creating contexts");

    server.createContext("/", new ExceptionWrapperHandler(new FileHandler(gson), gson));
    server.createContext(
        "/user/register",
        new ExceptionWrapperHandler(
            new SimpleRequestResponseHandler<>(
                (context) -> userService.registerUser(context.getBody()),
                UserRegisterRequest.class,
                gson),
            gson));
    server.createContext(
        "/user/login",
        new ExceptionWrapperHandler(
            new SimpleRequestResponseHandler<>(
                (context) -> userService.loginUser(context.getBody()),
                UserLoginRequest.class,
                gson),
            gson));
    server.createContext(
        "/person",
        new ExceptionWrapperHandler(
            new SimpleResponseHandler(
                "/person/{id}",
                (context) -> {
                  String id = context.getPathParams().get("id");
                  String token = context.getAuthToken();
                  if (id == null) {
                    return personService.getPersonsForCurrentUser(token);
                  }
                  return personService.getPerson(id, token);
                },
                gson),
            gson));
    server.createContext(
        "/event",
        new ExceptionWrapperHandler(
            new SimpleResponseHandler(
                "/event/{id}",
                (context) -> {
                  String id = context.getPathParams().get("id");
                  String token = context.getAuthToken();
                  if (id == null) {
                    return eventService.getEventsForCurrentUser(token);
                  }
                  return eventService.getEvent(id, token);
                },
                gson),
            gson));
    server.createContext(
        "/fill",
        new ExceptionWrapperHandler(
            new SimpleRequestResponseHandler<>(
                "/fill/{username}/{generations}",
                (context) -> {
                  String username = context.getPathParams().get("username");
                  String generationsString = context.getPathParams().get("generations");
                  int generations =
                      generationsString == null ? 4 : Integer.parseInt(generationsString);
                  return userService.fillUser(username, generations);
                },
                UserLoginRequest.class,
                gson),
            gson));
    server.createContext(
        "/load",
        new ExceptionWrapperHandler(
            new SimpleRequestResponseHandler<>(
                (context) -> rootService.loadData(context.getBody()), LoadDataRequest.class, gson),
            gson));
    server.createContext(
        "/clear",
        new ExceptionWrapperHandler(
            new SimpleResponseHandler((context) -> rootService.clearDatabase(), gson), gson));

    System.out.println("Starting server");

    server.start();

    System.out.println("Server started");
  }

  public static void main(String[] args) throws DataAccessException, IOException {
    String portNumber = args[0];
    new Server().run(portNumber);
  }
}
