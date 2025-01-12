package edu.uoc.epcsd.productcatalog.domain.service;

import edu.uoc.epcsd.productcatalog.domain.Product;
import edu.uoc.epcsd.productcatalog.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    public List<Product> findProductsByExample(Product product) {
        return productRepository.findProductsByExample(product);
    }

    public Long createProduct(Product product) {
        return productRepository.createProduct(product);
    }

    public void deleteProduct(Long id) {
    Optional<Product> productOptional = productRepository.findProductById(id);

    if (productOptional.isEmpty()) {
        log.warn("Product with id {} not found", id);
        throw new ProductNotFoundException(id);
    }

    Product product = productOptional.get();

    productRepository.deleteProduct(product);
    }
}
