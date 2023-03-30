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
import java.util.Objects;

import static com.increff.pos.util.ConvertFunction.*;

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
        OrderItemPojo pojo=convertOrderItemPojo(form);
        OrderPojo existingOrderPojo= orderService.get(pojo.getOrderId());
        if(existingOrderPojo.getInvoiceStatus()==1)
            throw new ApiException("Can't Add: Invoice is generated");


        ProductPojo existingProductPojo=productService.getCheck(form.getBarcode());
        InventoryPojo inventoryPojo=inventoryService.get(existingOrderPojo.getId());

        if(inventoryPojo.getQuantity() < pojo.getQuantity())
            throw new ApiException("This much quantity is not present. Max Quantity: "+inventoryPojo.getQuantity());

        pojo.setProductId(existingProductPojo.getId());
        if (pojo.getSellingPrice() > existingProductPojo.getMrp()) {
            throw new ApiException("Selling Price is greater than Mrp: " + existingProductPojo.getMrp());
        }

        service.add(pojo);
        inventoryService.decreaseInventory(inventoryService.get(pojo.getProductId()), pojo.getQuantity());

    }
    public void delete(int id) throws ApiException {
        OrderItemPojo p=service.get(id);

        OrderPojo orderPojo= orderService.get(p.getOrderId());
        if(orderPojo.getInvoiceStatus()==1) {
            throw new ApiException("Can't Delete: Invoice is generated");
        }
        InventoryPojo inventoryPojo=inventoryService.get(p.getProductId());
        service.delete(id);
        inventoryService.increaseInventory(inventoryPojo,p.getQuantity());
    }

    public List<OrderItemData> getByOrderId(int orderId){
        List<OrderItemData> orderItemDataList= new ArrayList<OrderItemData>();
        List<OrderItemPojo> orderItemPojoList= service.getByOrderId(orderId);

        for(OrderItemPojo pojo: orderItemPojoList){
            OrderItemData data=convertOrderItemData(pojo);
            ProductPojo productPojo=productService.get(pojo.getProductId());
            data.setBarcode(productPojo.getBarcode());
            orderItemDataList.add(data);
        }

        return orderItemDataList;

    }

    public OrderItemData get(int id){
        OrderItemData data=convertOrderItemData(service.get(id));
        ProductPojo productPojo=productService.get(data.getProductId());
        data.setBarcode(productPojo.getBarcode());
         return data;
    }

//    public List<OrderItemData> getAll(){
//        List<OrderItemData> orderItemDataList= new ArrayList<OrderItemData>();
//        List<OrderItemPojo> orderItemPojoList= service.getAll();
//        for(OrderItemPojo pojo: orderItemPojoList){
//            OrderItemData data=orderItemConvert(pojo);
//            ProductPojo productPojo=productService.get(pojo.getProductId());
//            data.setBarcode(productPojo.getBarcode());
//            orderItemDataList.add(data);
//        }
//        return orderItemDataList;
//    }


    public void update(int id,OrderItemForm form) throws ApiException{

        OrderItemPojo pojo=convertOrderItemPojo(form);
        OrderPojo existingOrderPojo= orderService.get(pojo.getOrderId());
        if(existingOrderPojo.getInvoiceStatus()==1)
            throw new ApiException("Can't Edit: Invoice is generated");

        ProductPojo existingProductPojo= productService.getCheck(form.getBarcode());

        InventoryPojo inventoryPojo=inventoryService.get(existingOrderPojo.getId());
        if(inventoryPojo.getQuantity() < pojo.getQuantity())
            throw new ApiException("This much quantity is not present. Max Quantity: "+inventoryPojo.getQuantity());

        if(pojo.getSellingPrice()>existingProductPojo.getMrp())
            throw new ApiException("Selling Price is greater than Mrp: "+existingProductPojo.getMrp());


        pojo.setProductId(existingProductPojo.getId());

        OrderItemPojo updatedPojo=service.get(id);

        service.update(updatedPojo,pojo);

        inventoryService.increaseInventory(inventoryPojo,updatedPojo.getQuantity());
        inventoryService.decreaseInventory(inventoryPojo,pojo.getQuantity());

    }




}


