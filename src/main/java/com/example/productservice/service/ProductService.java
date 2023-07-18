package com.example.productservice.service;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductQuantityPriceDTO;
import com.example.productservice.exception.BusinessLogicException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.example.productservice.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.example.productservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final RabbitTemplate rabbitTemplate;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    public ProductDTO saveProduct(ProductDTO productDTO) throws JsonProcessingException {
        ProductDTO productDTOCreated = createProductDTO(productRepository.save(this.createProduct(productDTO)));
        productDTOCreated.setPrice(productDTO.getPrice());
        productDTOCreated.setQuantity(productDTO.getQuantity());
        ProductQuantityPriceDTO quantityPriceDTO = createProductQuantityPriceDTO(productDTOCreated);
        rabbitTemplate.convertAndSend("product-inventory-save",objectMapper.writeValueAsString(quantityPriceDTO));
        return productDTOCreated;
    }

    public List<ProductDTO> getAllProducts() throws JsonProcessingException {
        List<ProductDTO> productDTOList = new ArrayList<>();
        String stringProductQuantityPriceDTO = (String) rabbitTemplate.convertSendAndReceive("product-inventory-getall", "getall");
        List<ProductQuantityPriceDTO> listProductQuantityPriceDTO = objectMapper.readValue(stringProductQuantityPriceDTO, new TypeReference<List<ProductQuantityPriceDTO>>(){});
        List<Product> listProduct = productRepository.findAll();
        if((stringProductQuantityPriceDTO != null) && (listProduct != null)){
            for (Product product : listProduct) {
                ProductDTO productDTO = createProductDTO(product);
                for(ProductQuantityPriceDTO productQuantityPriceDTO : listProductQuantityPriceDTO){
                    if(productDTO.getUuid().equals(productQuantityPriceDTO.getUuid())){
                        productDTOList.add(createProductDTOFromProductQuantityPriceDTO(productDTO,productQuantityPriceDTO));
                    }
                }
            }
        }
        else{
            throw new BusinessLogicException("Não há produtos disponíveis");
        }
        return productDTOList;
    }

    public ProductDTO createProductDTOFromProductQuantityPriceDTO(ProductDTO productDTO, ProductQuantityPriceDTO productQuantityPriceDTO){
        if (productQuantityPriceDTO.getQuantity() != null) {
            productDTO.setQuantity(productQuantityPriceDTO.getQuantity());
        }
        if (productQuantityPriceDTO.getPrice() != null) {
            productDTO.setPrice(productQuantityPriceDTO.getPrice());
        }
        return productDTO;
    }
    
    public ProductQuantityPriceDTO createProductQuantityPriceDTO(ProductDTO productDTO){
        ProductQuantityPriceDTO productQuantityPriceDTO = new ProductQuantityPriceDTO();
        if (productDTO.getUuid() != null) {
            productQuantityPriceDTO.setUuid(productDTO.getUuid());
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
            productDTO.setUuid(product.getId());
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

        if (productDTO.getUuid() != null) {
            product.setId(productDTO.getUuid());
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
