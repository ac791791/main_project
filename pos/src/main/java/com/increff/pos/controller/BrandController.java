package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/brands")
public class BrandController {


	@Autowired
	private BrandDto dto;
	
	@ApiOperation(value = "Adds a Brand and Category")
	@RequestMapping(method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		dto.add(form);
	}


	@ApiOperation(value="Gets a brand by id")
	@RequestMapping(value = "{id}",method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) {
		return dto.get(id);
	}

	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(method = RequestMethod.GET)
	public List<BrandData> getAll(){
		return dto.getAll();
	}

	@ApiOperation(value = "Gets list of brands of a given page")
	@RequestMapping(value = "/getLimited/{pageNo}",method = RequestMethod.GET)
	public List<BrandData> getLimited(@PathVariable int pageNo) throws ApiException {
		return dto.getLimited(pageNo);
	}

	@ApiOperation(value = "Getting total no of brands")
	@RequestMapping(value = "/total", method = RequestMethod.GET)
	public int totalBrands(){
		return dto.totalBrands();
	}

	@ApiOperation(value ="Updating Brand-Category")
	@RequestMapping(value ="/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
		dto.update(id,form);
	}

}
