package com.cdoropesa.api.dto;

import com.cdoropesa.api.model.enums.UserRole;

public record UserResponse(
    Long id,
    String email,
    String firstName,
    String lastName,
    String address,
    String city,
    String province,
    String country,
    String phone,
    UserRole role
) {}
