package response;

public final class MessageResponse extends BaseResponse {
  private final String message;

  public MessageResponse(boolean success, String message) {
    super(success);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
