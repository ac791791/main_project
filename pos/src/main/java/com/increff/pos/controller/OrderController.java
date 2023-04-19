package com.increff.pos.controller;


import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderDto dto;



    @ApiOperation(value = "Validating OrderForm")
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public int validateOrderForm(@RequestBody OrderForm form) throws ApiException {
        return dto.checkOrderForm(form);
    }

    @ApiOperation(value = "Add an Order with orderItems")
    @RequestMapping( method = RequestMethod.POST)
    public void add(@RequestBody List<OrderForm> forms) throws ApiException {
        dto.add(forms);
    }

    @ApiOperation(value = "Delete an order")
    @RequestMapping(value ="/{orderId}",method = RequestMethod.DELETE)
    public void delete(@PathVariable int orderId) throws ApiException {
        dto.delete(orderId);
    }

    @ApiOperation(value = "Get a order by OrderId")
    @RequestMapping(value ="/{orderId}", method = RequestMethod.GET)
    public OrderData get(@PathVariable int orderId) throws ApiException {
        return dto.get(orderId);
    }

    @ApiOperation(value = "Get list of Orders of a given page")
    @RequestMapping(value = "/getLimited/{pageNo}",method = RequestMethod.GET)
    public List<OrderData> getLimited(@PathVariable int pageNo) throws ApiException {
        return dto.getLimited(pageNo);
    }

    @ApiOperation(value = "Get total no of Orders")
    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public int totalOrders(){
        return dto.totalOrders();
    }



}
