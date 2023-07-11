package com.example.productservice.controller;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductQuantityPriceDTO;
import com.example.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class ProductRabbit {

    private ProductService productService;
//    @RabbitListener(queues = "product-inventory")
//    private List<ProductDTO> consumidor(){
//        return productService.getAllProducts();
//}
}

