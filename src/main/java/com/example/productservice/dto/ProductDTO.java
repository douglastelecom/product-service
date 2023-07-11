package com.example.productservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String uuid;
    private List<String> category;
    private String model;
    private BigDecimal price;
    private String description;
    private String brand;
    private String size;
    private Integer quantity;
}
