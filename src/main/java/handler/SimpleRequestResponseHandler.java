package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import response.BaseResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.function.Function;

public class SimpleRequestResponseHandler<RequestType> implements HttpHandler {
  private final Function<BodyRequestContext<RequestType>, BaseResponse> serviceHandler;
  private final Gson gson;
  private final Type requestType;
  private final URLPathPattern pattern;

  public SimpleRequestResponseHandler(
      String pattern,
      Function<BodyRequestContext<RequestType>, BaseResponse> serviceHandler,
      Class<RequestType> requestType,
      Gson gson) {
    this.pattern = new URLPathPattern(pattern);
    this.serviceHandler = serviceHandler;
    this.gson = gson;
    this.requestType = requestType;
  }

  public SimpleRequestResponseHandler(
      Function<BodyRequestContext<RequestType>, BaseResponse> serviceHandler,
      Class<RequestType> requestType,
      Gson gson) {
    this(null, serviceHandler, requestType, gson);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    RequestType request =
        gson.fromJson(HandlerUtil.readString(exchange.getRequestBody()), requestType);
    HandlerUtil.sendResponse(
        serviceHandler.apply(HandlerUtil.buildRequestContext(exchange, pattern).withBody(request)),
        exchange,
        gson);
  }
}
