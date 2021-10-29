package handler;

import java.util.Map;

public class BodyRequestContext<BodyType> extends RequestContext {
  private final BodyType body;

  public BodyRequestContext(BodyType body, String authToken, Map<String, String> pathParams) {
    super(authToken, pathParams);
    this.body = body;
  }

  public BodyType getBody() {
    return body;
  }
}
