package dsd.project.productscrud.service;

import dsd.project.productscrud.model.entity.Products;
import dsd.project.productscrud.model.repo.ProductsRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ProductsService implements IProductsService {

    private ProductsRepo productsRepo;

    @Override
    public List<Products> getProducts() {
        return productsRepo.findAll();
    }

    @Override
    public Optional<Products> getProductById(UUID id) {
        return productsRepo.findById(id);
    }

    @Override
    public Products createProduct(String name) {
        return productsRepo.save(new Products(name));
    }

    @Override
    public Optional<Products> updateProduct(UUID id, String name) {
        return productsRepo.findById(id).map(
                existingProduct -> {
                    Products updatedProduct = new Products(existingProduct.id(), name);
                    return productsRepo.save(updatedProduct);
                }
        );
    }

    @Override
    public void deleteProductById(UUID id) {
        productsRepo.deleteById(id);
    }
}
