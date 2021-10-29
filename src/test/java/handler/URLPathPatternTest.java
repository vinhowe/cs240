package handler;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class URLPathPatternTest {
  @Test
  public void parseUrl() {
    URLPathPattern pattern = new URLPathPattern("/user/{id}");
    Map<String, String> basicMatch = pattern.parse("/user/abc-def-ghi");
    assertEquals("abc-def-ghi", basicMatch.get("id"));

    URLPathPattern patternWithOptional = new URLPathPattern("/user/{id}/{generations}");
    Map<String, String> matchOmittedOptional = patternWithOptional.parse("/user/testabc");
    assertEquals("testabc", matchOmittedOptional.get("id"));
    assertFalse(matchOmittedOptional.containsKey("generations"));

    Map<String, String> matchWithOptional = patternWithOptional.parse("/user/testabc/5+2");
    assertEquals("testabc", matchWithOptional.get("id"));
    assertEquals("5+2", matchWithOptional.get("generations"));
  }

  @Test
  public void trailingSlashWorks() {
    URLPathPattern pattern = new URLPathPattern("/user/{id}");
    Map<String, String> basicMatch = pattern.parse("/user/abc-def-ghi/");
    assertEquals("abc-def-ghi", basicMatch.get("id"));
  }
}
