package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;

public class ExceptionWrapperHandler implements HttpHandler {
  HttpHandler handler;
  Gson gson;

  public ExceptionWrapperHandler(HttpHandler handler, Gson gson) {
    this.handler = handler;
    this.gson = gson;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      handler.handle(exchange);
    } catch (IOException | JsonSyntaxException e) {
      e.printStackTrace();
      HandlerUtil.sendErrorResponse(
          e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST, exchange, gson);
    } catch (Exception e) {
      e.printStackTrace();
      HandlerUtil.sendErrorResponse(
          e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR, exchange, gson);
    }
  }
}
