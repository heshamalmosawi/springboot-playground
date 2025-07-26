package com.springbootplayground.crudmongo.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springbootplayground.crudmongo.dto.ProductDTO;
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

    public Product create(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .userId(productDTO.getUserId())
                .build();

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
            product.setUserId(getCurrentUserId());
        }

        return prodRepo.save(product);
    }

    public Product update(String id, ProductDTO productDTO) {
        Product existingProduct = prodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (productDTO.getUserId() != null && !userRepo.existsById(productDTO.getUserId())) {
            throw new RuntimeException("User does not exist");
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setUserId(productDTO.getUserId());

        return prodRepo.save(existingProduct);
    }

    public void delete(String id) {
        Product existingProduct = prodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        prodRepo.delete(existingProduct);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        throw new RuntimeException("User not authenticated");
    }
}
