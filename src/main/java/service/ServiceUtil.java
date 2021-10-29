package service;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import model.AuthToken;
import response.MessageResponse;

public class ServiceUtil {
  public static TokenFetchResult attemptFetchAuthToken(String token, AuthTokenDAO authTokenDAO)
      throws DataAccessException {
    if (token == null) {
      return new TokenFetchResult(
          null, new MessageResponse(false, "Error: No auth token provided"));
    }
    AuthToken auth = authTokenDAO.find(token);
    if (auth == null) {
      return new TokenFetchResult(null, new MessageResponse(false, "Error: Invalid auth token"));
    }
    return new TokenFetchResult(auth, null);
  }

  public static class TokenFetchResult {
    private final AuthToken token;
    private final MessageResponse errorMessage;

    public TokenFetchResult(AuthToken token, MessageResponse errorMessage) {
      this.token = token;
      this.errorMessage = errorMessage;
    }

    public AuthToken getToken() {
      return token;
    }

    public MessageResponse getErrorMessage() {
      return errorMessage;
    }

    public boolean hasError() {
      return errorMessage != null;
    }
  }
}
