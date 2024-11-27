package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get all products with pagination.
     *
     * @param pageable - Pagination information.
     * @return A paginated list of products.
     */
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Get a product by ID.
     *
     * @param id - Product ID.
     * @return The product with the given ID.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /**
     * Create a new product.
     *
     * @param product - The product details.
     * @return The created product.
     */
    public Product createProduct(Product product) {
        // Validate that the associated category exists
        if (product.getCategory() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + product.getCategory().getId()));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    /**
     * Update a product by ID.
     *
     * @param id            - Product ID.
     * @param productDetails - Updated product details.
     * @return The updated product.
     */
    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(productDetails.getName());
        existingProduct.setPrice(productDetails.getPrice());

        // Update category if provided
        if (productDetails.getCategory() != null) {
            Category category = categoryRepository.findById(productDetails.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDetails.getCategory().getId()));
            existingProduct.setCategory(category);
        }

        return productRepository.save(existingProduct);
    }

    /**
     * Delete a product by ID.
     *
     * @param id - Product ID.
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
