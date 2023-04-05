package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.increff.pos.util.ConvertFunction.*;
import static com.increff.pos.util.InputChecks.*;

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

    public int checkOrderForm(OrderForm form) throws ApiException {
        ProductPojo productPojo=productService.getCheck(form.getBarcode());
        InventoryPojo inventoryPojo=inventoryService.get(productPojo.getId());
        validateOrderForm(form, productPojo,inventoryPojo);
        return inventoryPojo.getQuantity();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<OrderForm> forms) throws ApiException {
        if(forms.size()==0)
            throw new ApiException("Can't Add: No orderItem present ");
        OrderPojo orderPojo= new OrderPojo();
        orderPojo.setTime(java.time.LocalDateTime.now());
        orderPojo.setInvoiceStatus(0);
        service.addOrder(orderPojo);

        for(OrderForm form:forms){
            validateOrderForm(form);
            ProductPojo existingProductPojo= productService.getCheck(form.getBarcode());
            OrderItemPojo orderItemPojo = convertOrderItemPojo(orderPojo,form,existingProductPojo);
            orderItemService.add(orderItemPojo);
            inventoryService.decreaseInventory(existingProductPojo, form.getQuantity());
        }
    }
    public void delete(int orderId) throws ApiException {
        OrderPojo existingOrderPojo= service.getCheck(orderId);
        if(existingOrderPojo.getInvoiceStatus() == 1)
            throw new ApiException("Can't Delete: Invoice is generated");

        service.delete(orderId);
        List<OrderItemPojo> orderItemPojos= orderItemService.getByOrderId(orderId);
        for(OrderItemPojo p:orderItemPojos)
            inventoryService.increaseInventory(p.getProductId(),p.getQuantity());

        orderItemService.deleteByOrderId(orderId);
    }

    public OrderData get(int orderId) throws ApiException {
        return convertOrderData(service.getCheck(orderId));
    }

    public List<OrderData> getAll(){
        List<OrderData> orderDataList = new ArrayList<OrderData>();
        List<OrderPojo> orderPojoList = service.getAll();
        for(OrderPojo pojo:orderPojoList){
            orderDataList.add(convertOrderData(pojo));
        }
        return orderDataList;
    }
    public List<OrderData> getLimited(int pageNo) throws ApiException {
        if(pageNo<1)
            throw new ApiException("Page No can't be less than 1");
        List<OrderData> orderDataList = new ArrayList<OrderData>();
        List<OrderPojo> orderPojoList = service.getLimited(pageNo);
        for(OrderPojo pojo:orderPojoList){
            orderDataList.add(convertOrderData(pojo));
        }
        return orderDataList;
    }

    public int totalOrders(){
        return service.totalOrders();
    }

}

