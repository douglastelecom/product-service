package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductQuantityPriceDTO implements Serializable {
    public String id;
    public Integer quantity;
    public BigDecimal price;
}
