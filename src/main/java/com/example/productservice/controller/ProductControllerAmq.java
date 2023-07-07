package com.example.productservice.controller;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductQuantityPriceDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductControllerAmq {

    @RabbitListener(queues = "filaProductInventario")
    private void consumidor(ProductQuantityPriceDTO productQuantityPriceDTO){
        System.out.println("O ID é: "+productQuantityPriceDTO.getId());
        System.out.println("O preço é: "+productQuantityPriceDTO.getPrice());
        System.out.println("A quantidade é: "+productQuantityPriceDTO.getQuantity());
    }


}
