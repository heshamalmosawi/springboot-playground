package com.springbootplayground.crudmongo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springbootplayground.crudmongo.dto.ProductDTO;
import com.springbootplayground.crudmongo.model.Product;
import com.springbootplayground.crudmongo.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService prodService;

    public ProductsController(ProductService prodService) {
        this.prodService = prodService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> p = prodService.getAll();
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        try {
            Product product = prodService.getById(id);
            return ResponseEntity.ok(product);

        } catch (RuntimeException r) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO createProductDTO) {
        try {
            Product createdProduct = prodService.create(createProductDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDTO product) {
        try {
            Product updatedProduct = prodService.update(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException r) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        try {
            prodService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException r) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
