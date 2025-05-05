package com.mosuuuutech.CRUD.API.controllers;

import com.mosuuuutech.CRUD.API.entity.Product;
import com.mosuuuutech.CRUD.API.entity.User;
import com.mosuuuutech.CRUD.API.services.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add_Products")
    public Product addProduct(@RequestBody Product product){
        product.setPid(0);
        return productService.save(product);
    }

    @GetMapping("/products")
    public List<Product> getAllProduct(){
        return productService.findAll();
    }
    @GetMapping("/products/{Pid}")
    public Product getUser(@PathVariable int Pid) {
        return productService.findById(Pid);
    }

    @GetMapping("/products/number/{Pnum}")
    public ResponseEntity<?> getProductByNumber(@PathVariable int Pnum) {
        Optional<Product> product = productService.findByPnum(Pnum);

        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ไม่พบสินค้าที่มีรหัส " + Pnum, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update_product")
    public Product updateProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @DeleteMapping("/delete_product/{Pid}")
    public ResponseEntity<?> deleteProduct(@PathVariable int Pid){
        Product myProduct = productService.findById(Pid);

        if (myProduct==null){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put ("error", "Product id" + Pid + "Not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        String Pname = myProduct.getPname();
        productService.deleteById(Pid);
        Map<String, String> response = new HashMap<>();
        response.put("message","Delete  " + Pname + "  successfully");

        return ResponseEntity.ok(response);
    }

  }

