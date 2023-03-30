package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.increff.pos.util.ConvertFunction.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDto {

    @Autowired
    private OrderService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private InventoryService inventoryService;

    public void addOrder(){
        OrderPojo p= new OrderPojo();
        p.setTime(java.time.LocalDateTime.now());
        p.setInvoiceStatus(0);
        service.addOrder(p);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<OrderForm> forms) throws ApiException {
        OrderPojo orderPojo= new OrderPojo();
        orderPojo.setTime(java.time.LocalDateTime.now());
        orderPojo.setInvoiceStatus(0);
        service.addOrder(orderPojo);

        for(OrderForm form:forms){
            ProductPojo productPojo= productService.getCheck(form.getBarcode());

            OrderItemPojo orderItemPojo= new OrderItemPojo();
            orderItemPojo.setOrderId(orderPojo.getId());
            orderItemPojo.setQuantity(form.getQuantity());
            orderItemPojo.setSellingPrice(form.getSellingPrice());
            orderItemPojo.setProductId(productPojo.getId());

            orderItemService.add(orderItemPojo);
        }


    }
    public void delete(int orderId) throws ApiException {
        OrderPojo existingOrderPojo= service.get(orderId);
        if(existingOrderPojo.getInvoiceStatus()==1){
            throw new ApiException("Can't Delete: Invoice is generated");
        }
        service.delete(orderId);
        List<OrderItemPojo> orderItemPojos= orderItemService.getByOrderId(orderId);
        for(OrderItemPojo p:orderItemPojos){
            inventoryService.increaseInventory(inventoryService.get(p.getProductId()),p.getQuantity());
        }
        orderItemService.deleteByOrderId(orderId);
    }

    public OrderData get(int orderId){
        return convertOrderData(service.get(orderId));
    }

    public OrderData getRecentOrder(){
        return convertOrderData(service.getRecentOrder());
    }

    public List<OrderData> getAll(){
        List<OrderData> orderDataList= new ArrayList<OrderData>();
        List<OrderPojo> orderPojoList= service.getAll();
        for(OrderPojo pojo:orderPojoList){
            orderDataList.add(convertOrderData(pojo));
        }
        return orderDataList;
    }
    public List<OrderData> getLimited(int pageNo){
        List<OrderData> orderDataList= new ArrayList<OrderData>();
        List<OrderPojo> orderPojoList= service.getLimited(pageNo);
        for(OrderPojo pojo:orderPojoList){
            orderDataList.add(convertOrderData(pojo));
        }
        return orderDataList;
    }

    public int totalOrders(){
        return service.totalOrders();
    }

}

