package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import response.BaseResponse;

import java.io.IOException;
import java.util.function.Function;

public class SimpleResponseHandler implements HttpHandler {
  private final Function<RequestContext, BaseResponse> serviceHandler;
  private final Gson gson;
  private final URLPathPattern pattern;

  public SimpleResponseHandler(
      String pattern, Function<RequestContext, BaseResponse> serviceHandler, Gson gson) {
    this.pattern = new URLPathPattern(pattern);
    this.serviceHandler = serviceHandler;
    this.gson = gson;
  }

  public SimpleResponseHandler(Function<RequestContext, BaseResponse> serviceHandler, Gson gson) {
    this(null, serviceHandler, gson);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    BaseResponse response =
        serviceHandler.apply(HandlerUtil.buildRequestContext(exchange, pattern));
    HandlerUtil.sendResponse(response, exchange, gson);
  }
}
