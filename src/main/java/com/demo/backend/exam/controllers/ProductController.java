package com.demo.backend.exam.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.backend.exam.dto.ProductDTO;
import com.demo.backend.exam.services.product.ProductServices;
import com.demo.backend.exam.utilities.Constants;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE, required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {

        return new ResponseEntity<List<ProductDTO>>(
                productServices.getAllProducts(page, pageSize, sortBy, sortDirection),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {

        ProductDTO product = productServices.getProductById(id);
        return new ResponseEntity<ProductDTO>(product, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product) {
        return new ResponseEntity<ProductDTO>(productServices.addProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
            @RequestBody ProductDTO product) {

        ProductDTO productUpdated = productServices.updateProduct(id, product);
        return new ResponseEntity<ProductDTO>(productUpdated, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productServices.deleteProduct(id);
        return new ResponseEntity<String>("Product deleted successfully", HttpStatus.OK);
    }
}
