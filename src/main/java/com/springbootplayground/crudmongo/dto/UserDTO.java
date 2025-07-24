package com.springbootplayground.crudmongo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank private String name;
    @NotBlank private String email;
    @NotBlank private String password;
}