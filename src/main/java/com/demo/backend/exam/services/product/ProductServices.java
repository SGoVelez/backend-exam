package com.demo.backend.exam.services.product;

import java.util.List;

import com.demo.backend.exam.dto.ProductDTO;

public interface ProductServices {

    public List<ProductDTO> getAllProducts(Integer page, Integer pageSize, String sortBy, String sortDirection);

    public ProductDTO getProductById(Long id);

    public ProductDTO addProduct(ProductDTO product);

    public ProductDTO updateProduct(Long id, ProductDTO product);

    public void deleteProduct(Long id);
}
