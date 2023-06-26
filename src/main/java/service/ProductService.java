package service;

import dto.ProductDTO;
import exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import model.Product;
import org.springframework.stereotype.Service;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = this.createProduct(productDTO);
        return createProductDTO(productRepository.save(product));
    }
    public ProductDTO getProductByID(String id){
        Product product = productRepository.findById(id).get();
        return createProductDTO(product);
    }

    public List<ProductDTO> getAllProducts(){
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Product product:productRepository.findAll()){
            productDTOList.add(createProductDTO(product));
        }
        return productDTOList;
    }

    public ProductDTO createProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        if (product.getName() != null) {
            productDTO.setName(product.getName());
        }
        if (product.getDescription() != null) {
            productDTO.setDescription(product.getDescription());
        }
//        if (product.getPrice() != null) {
//            productDTO.setPrice(product.getPrice());
//        }
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
        if (productDTO.getName() != null) {
            product.setName(product.getName());
        } else {
            throw new BusinessLogicException("Nome do produto não informado.");
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(product.getDescription());
        } else {
            throw new BusinessLogicException("Descrição do produto não informada.");
        }
//        if (productDTO.getPrice() != null) {
//            product.setPrice(product.getPrice());
//        } else {
//            throw new BusinessLogicException("Preço do produto não informado.");
//        }
        if (productDTO.getBrand() != null) {
            product.setBrand(product.getBrand());
        } else {
            throw new BusinessLogicException("Marca do produto não informada.");
        }
        if (productDTO.getSize() != null) {
            product.setSize(product.getSize());
        } else {
            throw new BusinessLogicException("Tamanho do produto não informado.");
        }
        return product;
    }
}
