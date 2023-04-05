package com.increff.pos.controller;


import java.util.List;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryAddForm;
import com.increff.pos.model.InventoryUpdateForm;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.InventoryData;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {


	@Autowired
	private InventoryDto dto;

	@ApiOperation(value = "Gets a inventory by given id")
	@RequestMapping(value ="/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}
	
	@ApiOperation(value = "Gets list of Inventory of a given page")
	@RequestMapping(value = "/getLimited/{pageNo}",method = RequestMethod.GET)
	public List<InventoryData> getLimited(@PathVariable int pageNo) throws ApiException {
		return dto.getLimited(pageNo);
	}

	@ApiOperation(value = "Getting total no of Inventory")
	@RequestMapping(value = "/totalInventory", method = RequestMethod.GET)
	public int totalInventory(){
		return dto.totalInventory();
	}

	@ApiOperation(value = "Updating Table Inventory")
	@RequestMapping(value ="/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryUpdateForm form) throws ApiException {
		dto.update(id, form);
	}

	@ApiOperation(value = "Updating top Inventory")
	@RequestMapping(method = RequestMethod.PUT)
	public void topUpdate(@RequestBody InventoryAddForm form) throws ApiException {
		dto.topUpdate(form);
	}

}

