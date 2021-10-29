package response;

public abstract class BaseResponse {
  private final boolean success;

  public BaseResponse(boolean success) {
    this.success = success;
  }

  public boolean isSuccess() {
    return success;
  }
}
