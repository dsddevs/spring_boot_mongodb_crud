package dsd.project.productscrud.controller;

import dsd.project.productscrud.model.entity.Products;
import dsd.project.productscrud.service.IProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final IProductsService productsService;

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        try {
            return ResponseEntity.ok(productsService.getProducts());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable UUID id) {
        try {
            return productsService
                    .getProductById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Products> addProduct(@RequestBody CreateProductRequest request) {
        try {
            return ResponseEntity.ok(productsService.createProduct(request.name()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Products> updateProduct(
            @PathVariable UUID id,
            @RequestBody UpdateProductRequest request) {
        try {
            Optional<Products> updatedProduct = productsService.updateProduct(id, request.name());
            return updatedProduct
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        try {
            productsService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public record CreateProductRequest(String name) {
    }

    public record UpdateProductRequest(String name) {
    }
}