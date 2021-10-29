package handler;

import java.util.HashMap;
import java.util.Map;

public class URLPathPattern {
  private final String pattern;

  public URLPathPattern(String pattern) {
    this.pattern = pattern;
  }

  public String getPattern() {
    return pattern;
  }

  public Map<String, String> parse(String urlPath) {
    // Example URL path: /fill/{username}/{generations}
    if (pattern == null || urlPath == null) {
      return Map.of();
    }
    // Strip trailing slashes from URL path
    urlPath = urlPath.replaceAll("/$", "");

    Map<String, String> matches = new HashMap<>();
    int i = 0;
    int urlOffset = 0;
    int openIndex;
    int closeIndex;

    while (true) {
      openIndex = pattern.indexOf('{', i);
      closeIndex = pattern.indexOf('}', i);
      if (openIndex == -1 || closeIndex == -1) {
        break;
      }

      int nextUrlSlash = urlPath.indexOf('/', openIndex + urlOffset);
      if (nextUrlSlash == -1) {
        nextUrlSlash = urlPath.length();
      }
      if (openIndex + urlOffset > urlPath.length()) {
        break;
      }
      String key = pattern.substring(openIndex + 1, closeIndex);
      String value = urlPath.substring(openIndex + urlOffset, nextUrlSlash);
      matches.put(key, value);

      urlOffset += nextUrlSlash + urlOffset - closeIndex - 1;

      i = closeIndex + 1;
    }
    return matches;
  }
}
