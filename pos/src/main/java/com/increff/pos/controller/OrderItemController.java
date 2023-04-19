package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/orderItems")
public class OrderItemController {


    @Autowired
    private OrderItemDto dto;


    @ApiOperation(value = "Add a OrderItem")
    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody OrderItemForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Delete a order by its OrderItem id")
    @RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Get an order details by given OrderId")
    @RequestMapping(value ="/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getByOrderId(@PathVariable int orderId) throws ApiException {
        return dto.getByOrderId(orderId);
    }

    @ApiOperation(value = "Get orderItem by it's OrderItem Id")
    @RequestMapping(value = "/single/{id}", method = RequestMethod.GET)
    public OrderItemData getCheck(@PathVariable int id) throws ApiException {
        return dto.getCheck(id);
    }

    @ApiOperation(value = "Update an orderItem by it's id")
    @RequestMapping(value ="/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemForm form) throws ApiException{
        dto.update(id,form);
    }
}