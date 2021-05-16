package com.wirehall.commandhunt.backend.util;

public class SecurityUtil {
  private SecurityUtil() {
    // Utility classes should not have public constructors
  }

  /**
   * This method is helps to sanitize a string and make it safe for logging.
   *
   * <p>As per the sonar javasecurity:S5145 rule - User provided data, such as URL parameters, POST
   * data payloads or cookies, should always be considered untrusted and tainted. Applications
   * logging tainted data could enable an attacker to inject characters that would break the log
   * file pattern. This could be used to block monitors and SIEM (Security Information and Event
   * Management) systems from detecting other malicious events.
   *
   * @param string The string which needs to be sanitized
   * @return Sanitized string which is safe for logging
   */
  public static String sanitizeForLogging(String string) {
    if (string == null) return null;
    // javasecurity:S5145
    // This is to avoid logging user controlled data directly
    return string.replaceAll("[\n\r\t]", "_");
  }
}
