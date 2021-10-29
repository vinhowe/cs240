package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import response.BaseResponse;
import response.MessageResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class HandlerUtil {
  public static Logger logger = Logger.getLogger(HandlerUtil.class.getName());

  public static String readString(InputStream is) throws IOException {
    StringBuilder sb = new StringBuilder();
    InputStreamReader sr = new InputStreamReader(is);
    char[] buf = new char[1024];
    int len;
    while ((len = sr.read(buf)) > 0) {
      sb.append(buf, 0, len);
    }
    return sb.toString();
  }

  public static void writeString(String str, OutputStream os) throws IOException {
    OutputStreamWriter sw = new OutputStreamWriter(os);
    sw.write(str);
    sw.flush();
  }

  public static void sendErrorResponse(
      String error, int statusCode, HttpExchange exchange, Gson gson) throws IOException {
    exchange.sendResponseHeaders(statusCode, 0);
    OutputStream resBody = exchange.getResponseBody();
    HandlerUtil.writeString(
        gson.toJson(new MessageResponse(false, String.format("Error: %s", error))), resBody);
    exchange.getResponseBody().close();
  }

  public static void sendResponse(BaseResponse response, HttpExchange exchange, Gson gson)
      throws IOException {
    OutputStream responseBody = exchange.getResponseBody();
    exchange.sendResponseHeaders(
        response.isSuccess() ? HttpURLConnection.HTTP_OK : HttpURLConnection.HTTP_BAD_REQUEST, 0);
    String responseString = gson.toJson(response);
    logger.info(String.format("Sending response: %s", responseString));
    HandlerUtil.writeString(responseString, responseBody);
    responseBody.close();
  }

  public static RequestContext buildRequestContext(HttpExchange exchange, URLPathPattern pattern) {
    return new RequestContext(
        exchange.getRequestHeaders().getFirst("Authorization"),
        pattern.parse(exchange.getRequestURI().getPath()));
  }
}
