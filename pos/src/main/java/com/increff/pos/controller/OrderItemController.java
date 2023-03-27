package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/orderItem")
public class OrderItemController {


    @Autowired
    private OrderItemDto dto;


    @ApiOperation(value = "Adding a OrderItem")
    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody OrderItemForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Delete a order by its OrderItem id")
    @RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
    public void delete_id(@PathVariable int id) throws ApiException {
        dto.delete_id(id);
    }


    @ApiOperation(value = "Getting order details by given OrderId")
    @RequestMapping(value ="/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> get(@PathVariable int orderId) {

        return dto.get(orderId);

    }

    @ApiOperation(value = "Getting a order by Its OrderItem Id")
    @RequestMapping(value = "/single/{id}", method = RequestMethod.GET)
    public OrderItemData get_id(@PathVariable int id) {
        return dto.get_id(id);
    }


//    @ApiOperation(value = "Getting all order Details")
//    @RequestMapping( method = RequestMethod.GET)
//    public List<OrderItemData> getAll() {
//
//        return dto.getAll();
//    }

    @ApiOperation(value = "Updating an orderItem by its id")
    @RequestMapping(value ="/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemForm form) throws ApiException{
        dto.update(id,form);
    }
}