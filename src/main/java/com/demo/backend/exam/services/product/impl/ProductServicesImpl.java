package com.demo.backend.exam.services.product.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.backend.exam.dto.ProductDTO;
import com.demo.backend.exam.models.OrderItem;
import com.demo.backend.exam.models.Product;
import com.demo.backend.exam.repository.OrderItemRepository;
import com.demo.backend.exam.repository.ProductRepository;
import com.demo.backend.exam.services.product.ProductServices;
import com.demo.backend.exam.exceptions.NotFoundException;

@Service
public class ProductServicesImpl implements ProductServices {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProductDTO> getAllProducts(Integer page, Integer pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Product> productsEntities = productRepository.findAll(pageable);

        List<Product> productsEntitiesContent = productsEntities.getContent();
        // Entity to DTO
        List<ProductDTO> productsDTO = productsEntitiesContent.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return productsDTO;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product", "id", id));
        // Entity to DTO
        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);
        return productDTO;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        // DTO to Entity
        Product productEntity = modelMapper.map(productDTO, Product.class);
        // Entity to DTO
        Product savedProduct = productRepository.save(productEntity);
        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

        return savedProductDTO;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product productToUpdate = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product", "id", id));

        productToUpdate.setName(productDTO.getName());
        productToUpdate.setDescription(productDTO.getDescription());
        productToUpdate.setPrice(productDTO.getPrice());
        productToUpdate.setWeight(productDTO.getWeight());

        Product updatedProduct = productRepository.save(productToUpdate);

        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);

        return updatedProductDTO;
    }

    @Override
    public void deleteProduct(Long id) {

        Product productToDelete = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product", "id", id));

        List<OrderItem> orderItems = orderItemRepository.findByProduct(productToDelete);

        if (orderItems != null) {
            // trow 409 conflict
            throw new RuntimeException("Product is already in an order");
        }

        productRepository.delete(productToDelete);

    }
}
