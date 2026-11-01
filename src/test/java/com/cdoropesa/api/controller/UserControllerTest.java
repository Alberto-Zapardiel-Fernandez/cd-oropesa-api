package com.cdoropesa.api.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cdoropesa.api.model.UserEntity;
import com.cdoropesa.api.model.enums.UserRole;
import com.cdoropesa.api.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserControllerTest {

  @Test
  void userResponsesDoNotExposePassword() throws JsonProcessingException {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("admin@example.com")
        .password("stored-hash")
        .role(UserRole.ADMIN)
        .build();
    UserService userService = mock(UserService.class);
    when(userService.getAllUsers()).thenReturn(List.of(user));

    String response = new ObjectMapper().writeValueAsString(
        new UserController(userService).getAllUsers().getBody()
    );

    assertFalse(response.contains("password"));
    assertFalse(response.contains("stored-hash"));
  }
}
