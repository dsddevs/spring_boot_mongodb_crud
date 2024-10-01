package dsd.project.productscrud;

import dsd.project.productscrud.model.entity.Products;
import dsd.project.productscrud.model.repo.ProductsRepo;
import dsd.project.productscrud.service.ProductsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductsServiceTest {

    @Mock
    ProductsRepo productsRepo;

    @InjectMocks
    ProductsService productsService;

    private static final String PRODUCT_A_NAME = "Product_A";
    private static final String PRODUCT_B_NAME = "Product_B";
    private static final String UPDATED_PRODUCT_NAME = "Updated Product";

    @Test
    void testGetProducts() {
        List<Products> products = Arrays.asList(
                new Products(UUID.randomUUID(), PRODUCT_A_NAME),
                new Products(UUID.randomUUID(), PRODUCT_B_NAME));
        when(productsRepo.findAll()).thenReturn(products);

        List<Products> result = productsService.getProducts();

        assertEquals(2, result.size());
        verify(productsRepo).findAll();
    }

    @Test
    void testGetProductById() {
        UUID id = UUID.randomUUID();
        Products expectedProduct = new Products(id, PRODUCT_A_NAME);
        when(productsRepo.findById(id)).thenReturn(Optional.of(expectedProduct));

        Optional<Products> actualProduct = productsService.getProductById(id);

        assertTrue(actualProduct.isPresent());
        assertEquals(expectedProduct, actualProduct.get());
        verify(productsRepo).findById(id);
    }

    @Test
    void testGetProductByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(productsRepo.findById(id)).thenReturn(Optional.empty());

        Optional<Products> result = productsService.getProductById(id);

        assertTrue(result.isEmpty());
        verify(productsRepo).findById(id);
    }

    @Test
    void testAddProduct() {
        String productName = PRODUCT_A_NAME;
        Products savedProduct = new Products(UUID.randomUUID(), productName);

        ArgumentCaptor<Products> productCaptor = ArgumentCaptor.forClass(Products.class);
        when(productsRepo.save(productCaptor.capture())).thenReturn(savedProduct);

        Products createdProduct = productsService.createProduct(productName);

        assertNotNull(createdProduct.id());
        assertEquals(productName, createdProduct.name());

        Products capturedProduct = productCaptor.getValue();
        assertEquals(productName, capturedProduct.name());

        verify(productsRepo).save(any(Products.class));
    }
    @Test
    void testUpdateProduct() {
        UUID id = UUID.randomUUID();
        Products existingProduct = new Products(id, PRODUCT_A_NAME);
        Products updatedProduct = new Products(id, UPDATED_PRODUCT_NAME);
        when(productsRepo.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productsRepo.save(any(Products.class))).thenReturn(updatedProduct);

        Optional<Products> result = productsService.updateProduct(id, UPDATED_PRODUCT_NAME);

        assertTrue(result.isPresent());
        assertEquals(UPDATED_PRODUCT_NAME, result.get().name());
        verify(productsRepo).findById(id);
        verify(productsRepo).save(argThat(product ->
                product.id().equals(id) && product.name().equals(UPDATED_PRODUCT_NAME)
        ));
    }

    @Test
    void testUpdateProductNotFound() {
        UUID id = UUID.randomUUID();
        when(productsRepo.findById(id)).thenReturn(Optional.empty());

        Optional<Products> result = productsService.updateProduct(id, UPDATED_PRODUCT_NAME);

        assertTrue(result.isEmpty());
        verify(productsRepo).findById(id);
        verify(productsRepo, never()).save(any());
    }

    @Test
    void testDeleteProductByIdThrowsException() {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Delete failed")).when(productsRepo).deleteById(id);

        assertThrows(RuntimeException.class, () -> productsService.deleteProductById(id));
        verify(productsRepo).deleteById(id);
    }
}