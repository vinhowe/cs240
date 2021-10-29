package model;

import java.util.Objects;
import java.util.UUID;

public class AuthToken {
  private UUID token;
  private String username;

  public AuthToken(UUID token, String username) {
    this.token = token;
    this.username = username;
  }

  /** Get the token */
  public UUID getToken() {
    return token;
  }

  /** Set the token */
  public void setToken(UUID token) {
    this.token = token;
  }

  /** Get the username */
  public String getUsername() {
    return username;
  }

  /** Set the username */
  public void setUsername(String username) {
    this.username = username;
  }

  public static AuthToken generate(String username) {
    return new AuthToken(UUID.randomUUID(), username);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthToken authToken = (AuthToken) o;
    return Objects.equals(token, authToken.token) && Objects.equals(username, authToken.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, username);
  }
}
