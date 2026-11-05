package com.cdoropesa.api.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtAuthenticationFilterTest {

  @Test
  void rejectsInvalidTokenWithUnauthorizedWithoutContinuingFilterChain() throws Exception {
    JwtService jwtService = mock(JwtService.class);
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    when(jwtService.extractUsername("invalid-token"))
        .thenThrow(new JwtException("Invalid token"));
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer invalid-token");
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    filter.doFilterInternal(request, response, filterChain);

    assertEquals(401, response.getStatus());
    verify(filterChain, never()).doFilter(request, response);
  }
}
