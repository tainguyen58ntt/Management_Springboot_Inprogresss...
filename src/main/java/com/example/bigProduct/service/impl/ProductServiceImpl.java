package com.example.bigProduct.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bigProduct.repository.ProductRepository;
import com.example.bigProduct.service.ProductService;

@Service 
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductRepository productRepository;
	
}
