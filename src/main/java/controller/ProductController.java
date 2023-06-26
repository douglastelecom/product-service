package controller;

import dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductDTO productDTO){
        productService.saveProduct(productDTO);
    }


}
