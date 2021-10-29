package handler;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

public class FileHandler implements HttpHandler {
  private static final Path NOT_FOUND_FILE =
      FileSystems.getDefault().getPath("./web/HTML/404.html");

  private final Gson gson;

  public FileHandler(Gson gson) {
    this.gson = gson;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
      HandlerUtil.sendErrorResponse(
          String.format(
              "Unsupported method: %s", exchange.getRequestMethod().toUpperCase(Locale.ROOT)),
          HttpURLConnection.HTTP_BAD_REQUEST,
          exchange,
          gson);
    }

    String urlPath = exchange.getRequestURI().toString();
    if (urlPath == null || urlPath.equals("/")) {
      urlPath = "/index.html";
    }
    Path filePath = FileSystems.getDefault().getPath("./web/", urlPath.substring(1));
    boolean fileExists = Files.exists(filePath);
    exchange.sendResponseHeaders(
        fileExists ? HttpURLConnection.HTTP_OK : HttpURLConnection.HTTP_NOT_FOUND, 0);
    Files.copy(fileExists ? filePath : NOT_FOUND_FILE, exchange.getResponseBody());
    exchange.getResponseBody().close();
  }
}
