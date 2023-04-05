package com.increff.pos.dto;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;


import static com.increff.pos.util.ConvertFunction.*;
import static com.increff.pos.util.InputChecks.checkOrderItemParameters;
import static com.increff.pos.util.InputChecks.validateOrderItemForm;

@Repository
public class OrderItemDto {

    @Autowired
    private OrderItemService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;


    public void add(OrderItemForm form) throws ApiException {
        validateOrderItemForm(form);
        OrderItemPojo pojo = convertOrderItemPojo(form);
        OrderPojo existingOrderPojo = orderService.getCheck(pojo.getOrderId());
        ProductPojo existingProductPojo=productService.getCheck(form.getBarcode());
        InventoryPojo inventoryPojo=inventoryService.get(existingProductPojo.getId());

        pojo.setProductId(existingProductPojo.getId());
        checkOrderItemParameters(pojo,existingOrderPojo,existingProductPojo,inventoryPojo);
        service.add(pojo);
        inventoryService.decreaseInventory(existingProductPojo, pojo.getQuantity());

    }
    public void delete(int id) throws ApiException {
        OrderItemPojo p=service.getCheck(id);

        OrderPojo orderPojo= orderService.getCheck(p.getOrderId());
        if(orderPojo.getInvoiceStatus()==1) {
            throw new ApiException("Can't Delete: Invoice is generated");
        }
        InventoryPojo inventoryPojo=inventoryService.get(p.getProductId());
        service.delete(id);
        inventoryService.increaseInventory(p.getProductId(),p.getQuantity());
    }

    public List<OrderItemData> getByOrderId(int orderId) throws ApiException {
        OrderPojo orderPojo = orderService.getCheck(orderId);
        List<OrderItemData> orderItemDataList= new ArrayList<OrderItemData>();
        List<OrderItemPojo> orderItemPojoList= service.getByOrderId(orderId);

        for(OrderItemPojo pojo: orderItemPojoList){
            ProductPojo productPojo=productService.getCheck(pojo.getProductId());
            OrderItemData data=convertOrderItemData(pojo,productPojo);
            orderItemDataList.add(data);
        }

        return orderItemDataList;

    }

    public OrderItemData getCheck(int id) throws ApiException {
        OrderItemPojo pojo=service.getCheck(id);
        ProductPojo productPojo=productService.getCheck(pojo.getProductId());
        OrderItemData data=convertOrderItemData(pojo,productPojo);
        return data;
    }


    public void update(int id,OrderItemForm form) throws ApiException{
        validateOrderItemForm(form);
        OrderItemPojo pojo=convertOrderItemPojo(form);
        OrderPojo existingOrderPojo= orderService.getCheck(pojo.getOrderId());
        ProductPojo existingProductPojo= productService.getCheck(form.getBarcode());
        InventoryPojo inventoryPojo=inventoryService.get(existingProductPojo.getId());
        pojo.setProductId(existingProductPojo.getId());

        checkOrderItemParameters(pojo,existingOrderPojo,existingProductPojo,inventoryPojo);
        OrderItemPojo updatedPojo=service.getCheck(id);
        service.update(id,pojo);
        inventoryService.increaseInventory(existingProductPojo.getId(),updatedPojo.getQuantity());
        inventoryService.decreaseInventory(existingProductPojo,pojo.getQuantity());

    }

}


