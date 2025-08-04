package com.example.ecommerce.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
public class UserDto {
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    private String name;
    private String address;
    private String phone;
}
