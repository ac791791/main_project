package com.increff.employee.controller;

import java.util.List;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/brand")
public class BrandController {


	@Autowired
	private BrandDto dto;
	
	@ApiOperation(value = "Adding a Brand: Category")
	@RequestMapping(method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		dto.add(form);
	}
	

	@ApiOperation(value="Getting a brand by id")
	@RequestMapping(value = "{id}",method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) {
		return dto.get(id);
	}
	
	@ApiOperation(value = "Getting all brands")
	@RequestMapping(method = RequestMethod.GET)
	public List<BrandData> getAll(){
		return dto.getAll();
	}

	@ApiOperation(value = "Getting limit brands of a given page")
	@RequestMapping(value = "/getLimited/{page}",method = RequestMethod.GET)
	public List<BrandData> getLimited(@PathVariable int page){
		return dto.getLimited(page);
	}

	@ApiOperation(value = "Getting total no of brands")
	@RequestMapping(value = "/totalBrands", method = RequestMethod.GET)
	public int totalBrands(){
		return dto.totalBrands();
	}
	
	@ApiOperation(value ="Updating Brand-Category")
	@RequestMapping(value ="/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
		dto.update(id,form);
		
	}

}
