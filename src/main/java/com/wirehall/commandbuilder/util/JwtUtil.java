package com.wirehall.commandbuilder.util;

import com.wirehall.commandbuilder.model.auth.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

  @Value("${app.jwt.secret}")
  private String appJwtSecret;

  /**
   * Creates a JWT token with specified information.
   *
   * @param authentication Used to get the principal required in token creation.
   * @param expiry         Token expiry duration.
   * @return JWT token.
   */
  public String createToken(Authentication authentication, Long expiry) {
    CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiry);

    return Jwts.builder()
        .setSubject(customUserPrincipal.getEmail())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, appJwtSecret)
        .compact();
  }

  /**
   * Retrieve user's email from token.
   *
   * @param token JWT token.
   * @return User's email address.
   */
  public String getUserEmailFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(appJwtSecret).parseClaimsJws(token).getBody();

    return claims.getSubject();
  }

  /**
   * Validates the specified token.
   *
   * @param authToken JWT token.
   * @return true if token is valid, false otherwise.
   */
  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(appJwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      LOGGER.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      LOGGER.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      LOGGER.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      LOGGER.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      LOGGER.error("JWT claims string is empty.");
    }
    return false;
  }
}
