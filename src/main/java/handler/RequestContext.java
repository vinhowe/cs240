package handler;

import java.util.Map;

public class RequestContext {
  private final String authToken;
  private final Map<String, String> pathParams;

  public RequestContext(String authToken, Map<String, String> pathParams) {
    this.authToken = authToken;
    this.pathParams = pathParams;
  }

  public <T> BodyRequestContext<T> withBody(T body) {
    return new BodyRequestContext<>(body, authToken, pathParams);
  }

  public String getAuthToken() {
    return authToken;
  }

  public Map<String, String> getPathParams() {
    return pathParams;
  }
}
