package com.springbootplayground.crudmongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springbootplayground.crudmongo.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId);
}
