package dsd.project.productscrud.service;

import dsd.project.productscrud.model.entity.Products;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductsService {

    List<Products> getProducts();
    Optional<Products> getProductById(UUID id);
    Products createProduct(String name);
    Optional<Products> updateProduct(UUID id, String name);
    void deleteProductById(UUID id);
}
