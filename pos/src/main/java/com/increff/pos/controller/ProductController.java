package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/products")
public class ProductController {


	@Autowired
	private ProductDto dto;
	
	@ApiOperation(value = "Add a Product")
	@RequestMapping(method =RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Get a product of a given id")
	@RequestMapping(value ="/{id}", method=RequestMethod.GET)
	public ProductData get(@PathVariable int id) {
		return dto.get(id);
	}
	
//	@ApiOperation(value = "Getting all Products")
//	@RequestMapping(method = RequestMethod.GET)
//	public List<ProductData> getAll(){
//		return dto.getAll();
//	}

	@ApiOperation(value = "Get list of products of a given page")
	@RequestMapping(value = "/getLimited/{pageNo}",method = RequestMethod.GET)
	public List<ProductData> getLimited(@PathVariable int pageNo){
		return dto.getLimited(pageNo);
	}


	@ApiOperation(value = "Get total no of products")
	@RequestMapping(value = "/total", method = RequestMethod.GET)
	public int totalProducts(){
		return dto.totalProducts();
	}

	//TODO: to use updateProductForm
	@ApiOperation(value = "Update a Product")
	@RequestMapping(value ="/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
		dto.update(id,form);
	}
	
	
	

}
