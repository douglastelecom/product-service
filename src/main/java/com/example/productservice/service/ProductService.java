package com.example.productservice.service;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductQuantityPriceDTO;
import com.example.productservice.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import com.example.productservice.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.productservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final RabbitTemplate rabbitTemplate;

    private final ProductRepository productRepository;

    public ProductDTO saveProduct(ProductDTO productDTO) {

        ProductDTO productDTOCreated = createProductDTO(productRepository.save(this.createProduct(productDTO)));
        productDTOCreated.setPrice(productDTO.getPrice());
        productDTOCreated.setQuantity(productDTO.getQuantity());
        rabbitTemplate.convertAndSend("filaProductInventario",createProductQuantityPriceDTO(productDTOCreated));
        return productDTOCreated;
    }

    public List<ProductDTO> getAllProducts() {
        //solicita o preço para o inventário
        //setar o preço de todos os produtos no DTO antes de retornar ao cliente
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            productDTOList.add(createProductDTO(product));
        }
        return productDTOList;
    }

    public ProductDTO createProductDTOFromProductQuantityPriceDTO(ProductDTO productDTO, ProductQuantityPriceDTO productQuantityPriceDTO){
        if (productDTO.getQuantity() != null) {
            productDTO.setQuantity(productDTO.getQuantity());
        }
        if (productDTO.getPrice() != null) {
            productDTO.setPrice(productDTO.getPrice());
        }
        return productDTO;
    }
    
    public ProductQuantityPriceDTO createProductQuantityPriceDTO(ProductDTO productDTO){
        ProductQuantityPriceDTO productQuantityPriceDTO = new ProductQuantityPriceDTO();
        if (productDTO.getId() != null) {
            productQuantityPriceDTO.setId(productDTO.getId());
        }
        if (productDTO.getQuantity() != null) {
            productQuantityPriceDTO.setQuantity(productDTO.getQuantity());
        } else {
            throw new BusinessLogicException("Informe a quantidade do produto.");
        }
        if (productDTO.getPrice() != null) {
            productQuantityPriceDTO.setPrice(productDTO.getPrice());
        } else {
            throw new BusinessLogicException("Informe o preço do produto.");
        }
        return productQuantityPriceDTO;
    }

    public ProductDTO createProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        if (product.getCategory() != null) {
            productDTO.setCategory(product.getCategory());
        }
        if (product.getModel() != null) {
            productDTO.setModel(product.getModel());
        }
        if (product.getId() != null) {
            productDTO.setId(product.getId());
        }
        if (product.getDescription() != null) {
            productDTO.setDescription(product.getDescription());
        }
        if (product.getBrand() != null) {
            productDTO.setBrand(product.getBrand());
        }
        if (product.getSize() != null) {
            productDTO.setSize(product.getSize());
        }
        return productDTO;
    }

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();

        if (productDTO.getId() != null) {
            product.setId(productDTO.getId());
        }
        if (productDTO.getCategory() != null) {
            product.setCategory(productDTO.getCategory());
        } else {
            throw new BusinessLogicException("Informe ao menos uma categoria do produto.");
        }
        if (productDTO.getModel() != null) {
            product.setModel(productDTO.getModel());
        } else {
            throw new BusinessLogicException("Modelo do produto não informado.");
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        } else {
            throw new BusinessLogicException("Descrição do produto não informada.");
        }

        if (productDTO.getBrand() != null) {
            product.setBrand(productDTO.getBrand());
        } else {
            throw new BusinessLogicException("Marca do produto não informada.");
        }
        if (productDTO.getSize() != null) {
            product.setSize(productDTO.getSize());
        } else {
            throw new BusinessLogicException("Tamanho do produto não informado.");
        }
        return product;
    }
}
