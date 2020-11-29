package com.wirehall.commandbuilder.util;

import java.util.Base64;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

public class CookieUtil {

  private CookieUtil() {
    // Utility classes should not have public constructors
  }

  /**
   * Returns the matching cookie by name.
   *
   * @param request The request instance.
   * @param name    The name of the cookie to be retrieved.
   * @return The matched cookie.
   */
  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();

    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          return Optional.of(cookie);
        }
      }
    }

    return Optional.empty();
  }

  /**
   * Adds the cookie to response instance.
   *
   * @param response The response instance.
   * @param name     The name of the cookie.
   * @param value    The value of the cookie.
   * @param maxAge   Validity time of the cookie.
   */
  public static void addCookie(HttpServletResponse response, String name, String value,
      int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  /**
   * Deletes the cookie with matching name.
   *
   * @param request  The request instance.
   * @param response The response instance.
   * @param name     The name of the cookie to be deleted.
   */
  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
      String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          cookie.setValue("");
          cookie.setPath("/");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        }
      }
    }
  }

  /**
   * Serializes the object and encodes using Base64 encoding.
   *
   * @param object The object to be serialized.
   * @return The serialized string in base64 format.
   */
  public static String serialize(Object object) {
    return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
  }

  /**
   * Deserialize the cookie value and cast it to the specified class type.
   *
   * @param cookie The cookie to deserialize.
   * @param cls    The type of class to cast the deserialize the result.
   * @return Object created out of deserialization.
   */
  public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    return cls
        .cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
  }
}
