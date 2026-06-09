package com.amc.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.amc.api.dto.response.ProductDTO;
import com.amc.api.entities.Product;
import com.amc.api.repositories.ProductRepository;
import com.amc.api.utils.Exceptions;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProductShouldSaveWhenValidationPasses() {
        Product product = buildProduct();

        when(productRepository.findByUuid(product.getUuid())).thenReturn(product);
        when(productRepository.findByName(product.getName())).thenReturn(null);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertSame(product, result);
        verify(productRepository).save(product);
    }

    @Test
    void createProductShouldThrowWhenProductDoesNotExistInValidation() {
        Product product = buildProduct();

        when(productRepository.findByUuid(product.getUuid())).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(
                Exceptions.ResourceNotFoundException.class,
                () -> productService.createProduct(product));

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void createProductShouldThrowWhenNameAlreadyExists() {
        Product product = buildProduct();

        when(productRepository.findByUuid(product.getUuid())).thenReturn(product);
        when(productRepository.findByName(product.getName())).thenReturn(new Product());

        Exceptions.DatabaseException exception = assertThrows(
                Exceptions.DatabaseException.class,
                () -> productService.createProduct(product));

        assertEquals("Erro no banco de dados: Produto já existe", exception.getMessage());
    }

    @Test
    void updateProductShouldMapAndSaveWhenProductExists() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Novo nome");
        Product product = buildProduct();

        when(productRepository.findByUuid(product.getUuid())).thenReturn(product);
        when(productRepository.findByName(product.getName())).thenReturn(null);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(dto, product.getUuid());

        assertSame(product, result);
        verify(mapper).map(dto, product);
        verify(productRepository).save(product);
    }

    @Test
    void updateProductShouldWrapUnexpectedErrors() {
        ProductDTO dto = new ProductDTO();
        Product product = buildProduct();
        RuntimeException failure = new RuntimeException("mapper-failed");

        when(productRepository.findByUuid(product.getUuid())).thenReturn(product);
        when(productRepository.findByName(product.getName())).thenReturn(null);
        doThrow(failure).when(mapper).map(dto, product);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.updateProduct(dto, product.getUuid()));

        assertSame(failure, exception.getCause());
    }

    @Test
    void deleteProductShouldReturnTrueWhenRepositoryDeletes() {
        boolean result = productService.deleteProduct("product-uuid");

        assertTrue(result);
        verify(productRepository).deleteProductByUuid("product-uuid");
    }

    @Test
    void deleteProductShouldWrapRepositoryErrors() {
        RuntimeException failure = new RuntimeException("delete-failed");
        doThrow(failure).when(productRepository).deleteProductByUuid("product-uuid");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.deleteProduct("product-uuid"));

        assertSame(failure, exception.getCause());
    }

    @Test
    void validateProductShouldThrowWhenUuidIsNotFound() {
        Product product = buildProduct();

        when(productRepository.findByUuid(product.getUuid())).thenReturn(null);

        assertThrows(Exceptions.ResourceNotFoundException.class, () -> productService.validateProduct(product));
    }

    @Test
    void validateProductShouldThrowWhenAnotherProductHasSameName() {
        Product product = buildProduct();

        when(productRepository.findByUuid(product.getUuid())).thenReturn(product);
        when(productRepository.findByName(product.getName())).thenReturn(new Product());

        assertThrows(Exceptions.DatabaseException.class, () -> productService.validateProduct(product));
    }

    private Product buildProduct() {
        Product product = new Product();
        product.setUuid("product-uuid");
        product.setName("Notebook");
        product.setPrice(1200.0);
        product.setEstoque(10);
        product.setSkuCode("SKU-001");
        product.setKeywords("tech");
        return product;
    }
}
