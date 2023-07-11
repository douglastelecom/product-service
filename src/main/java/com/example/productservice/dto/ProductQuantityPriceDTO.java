package com.example.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductQuantityPriceDTO implements Serializable {
    private String uuid;
    private Integer quantity;
    private BigDecimal price;
}
