package com.increff.pos.controller;


import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.BrandData;
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
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderDto dto;


    @ApiOperation(value = "Adding a Order like edit order")
    @RequestMapping(method = RequestMethod.POST)
    public void addOrder(){
        dto.addOrder();
    }

    @ApiOperation(value = "Deleting a order")
    @RequestMapping(value ="/{orderId}",method = RequestMethod.DELETE)
    public void delete(@PathVariable int orderId) throws ApiException {
        dto.delete(orderId);
    }

    @ApiOperation(value = "Getting a order by OrderId")
    @RequestMapping(value ="/{orderId}", method = RequestMethod.GET)
    public OrderData get(@PathVariable int orderId){
        return dto.get(orderId);
    }

    @ApiOperation(value = "Getting max order id or recent order")
    @RequestMapping(value ="/recentOrder",method = RequestMethod.GET)
    public OrderData getRecentOrder(){
        return dto.getRecentOrder();
    }


//    @ApiOperation(value = "Getting all orders")
//    @RequestMapping(method = RequestMethod.GET)
//    public List<OrderData> getAll(){
//        return dto.getAll();
//    }

    @ApiOperation(value = "Getting limit Orders of a given page")
    @RequestMapping(value = "/getLimited/{page}",method = RequestMethod.GET)
    public List<OrderData> getLimited(@PathVariable int page){
        return dto.getLimited(page);
    }

    @ApiOperation(value = "Getting total no of Orders")
    @RequestMapping(value = "/totalOrders", method = RequestMethod.GET)
    public int totalOrders(){
        return dto.totalOrders();
    }



}
