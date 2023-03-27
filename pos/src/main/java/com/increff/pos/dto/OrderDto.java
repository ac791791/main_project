package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static com.increff.pos.util.ConvertFunction.*;

import java.util.ArrayList;
import java.util.List;

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
    public void delete(int orderId) throws ApiException {
        OrderPojo orderPojo= service.get(orderId);
        if(orderPojo.getInvoiceStatus()==1){
            throw new ApiException("Can't Delete: Invoice is generated");
        }
        service.delete(orderId);
        List<OrderItemPojo> orderItemPojos= orderItemService.get(orderId);
        for(OrderItemPojo p:orderItemPojos){
            inventoryService.increaseInventory(inventoryService.get(p.getProductId()),p.getQuantity());
        }
        orderItemService.delete(orderId);
    }

    public OrderData get(int orderId){
        return orderConvert(service.get(orderId));
    }

    public OrderData getRecentOrder(){
        return orderConvert(service.getRecentOrder());
    }

    public List<OrderData> getAll(){
        List<OrderData> list1= new ArrayList<OrderData>();
        List<OrderPojo> list2= service.getAll();
        for(OrderPojo pojo:list2){
            list1.add(orderConvert(pojo));
        }
        return list1;
    }
    public List<OrderData> getLimited(int page){
        List<OrderData> list1= new ArrayList<OrderData>();
        List<OrderPojo> list2= service.getLimited(page);
        for(OrderPojo pojo:list2){
            list1.add(orderConvert(pojo));
        }
        return list1;
    }

    public int totalOrders(){
        return service.totalOrders();
    }

}

