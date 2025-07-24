package com.springbootplayground.crudmongo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDTO {
    @NotBlank private String name;
    @NotBlank private String description;
    @NotNull private Double price;
    @NotBlank private String userId;
}