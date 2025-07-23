package com.springbootplayground.crudmongo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springbootplayground.crudmongo.model.Product;
import com.springbootplayground.crudmongo.repository.ProductRepository;
import com.springbootplayground.crudmongo.repository.UserRepository;

@Service
public class ProductService {

    private final ProductRepository prodRepo;
    private final UserRepository userRepo;

    public ProductService(ProductRepository prodRepository, UserRepository userRepository) {
        this.prodRepo = prodRepository;
        this.userRepo = userRepository;
    }

    public List<Product> getAll() {
        return prodRepo.findAll();
    }

    public Product getById(String id) {
        return prodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product create(Product product) { 
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }
        if (product.getPrice() <= 0) {
            throw new RuntimeException("Product price must be greater than zero");
        }
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new RuntimeException("Product description is required");
        }

        if (product.getUserId() == null || product.getUserId().isEmpty()) {
            throw new RuntimeException("User ID is required");
        }

        if (!userRepo.existsById(product.getUserId())) {
            throw new RuntimeException("User does not exist");
        }

        return prodRepo.save(product);
     }
    public Product update(String id, Product product) { 
        Product existingProduct = prodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getName() != null && !product.getName().isEmpty()) {
            existingProduct.setName(product.getName());
        }
        if (product.getPrice() > 0) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getUserId() != null && !product.getUserId().isEmpty()) {
            if (!userRepo.existsById(product.getUserId())) {
                throw new RuntimeException("User does not exist");
            }
            existingProduct.setUserId(product.getUserId());
        }

        return prodRepo.save(existingProduct);
     }
     
    public void delete(String id) {
        Product existingProduct = prodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        prodRepo.delete(existingProduct);
    }
}
