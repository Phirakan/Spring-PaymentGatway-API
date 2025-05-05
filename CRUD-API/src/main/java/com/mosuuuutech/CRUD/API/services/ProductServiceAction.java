package com.mosuuuutech.CRUD.API.services;

import com.mosuuuutech.CRUD.API.entity.Product;
import com.mosuuuutech.CRUD.API.entity.User;
import com.mosuuuutech.CRUD.API.repository.ProductRepository;
import com.mosuuuutech.CRUD.API.services.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceAction implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceAction(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Integer Pid) {
        Optional<Product> result = productRepository.findById(Pid);
        Product Pdata = null;
        if (result.isPresent()) {
            Pdata = result.get();
        } else {
            throw new RuntimeException("ไม่พบสินค้า" + Pid);
        }
        return Pdata;
    }

    @Override
    public Optional<Product> findByPnum(Integer Pnum) {
        return productRepository.findByPnum(Pnum);
    }

    @Override
    public void deleteById(Integer Pid) {
        productRepository.deleteById(Pid);
    }



}
