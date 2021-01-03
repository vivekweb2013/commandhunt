package com.wirehall.commandhunt.backend.security;

import com.wirehall.commandhunt.backend.service.auth.CustomOAuthUserService;
import com.wirehall.commandhunt.backend.service.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private CustomOAuthUserService customOAuthUserService;

  @Autowired
  private OAuthSuccessHandler oauthSuccessHandler;

  @Autowired
  private OAuthFailureHandler oauthFailureHandler;

  @Bean
  public CustomJwtAuthFilter customJwtAuthFilter() {
    return new CustomJwtAuthFilter();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder
        .userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .httpBasic()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .and()
        .authorizeRequests()
        .antMatchers(
            "/**", // TODO: Remove the ** later
            "/error",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js")
        .permitAll()
        .antMatchers("/api/auth/**", "/api/oauth2/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/api/oauth2/authorize")
        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
        .and()
        .redirectionEndpoint()
        .baseUri("/api/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .userService(customOAuthUserService)
        .and()
        .successHandler(oauthSuccessHandler)
        .failureHandler(oauthFailureHandler);

    // Add our custom Token based authentication filter
    http.addFilterBefore(customJwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
  }


  /**
   * By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save the
   * authorization request. But, since our service is stateless, we can't save it in the session.
   * We'll save the request in a Base64 encoded cookie instead.
   *
   * @return Custom repository for storing and retrieving the request from cookies.
   */
  @Bean
  public CustomOAuthRequestRepository cookieAuthorizationRequestRepository() {
    return new CustomOAuthRequestRepository();
  }
}
