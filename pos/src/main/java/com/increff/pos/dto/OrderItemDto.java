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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConvertFunction.orderItemConvert;

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


    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderItemForm form) throws ApiException {
        OrderItemPojo p=orderItemConvert(form);
        OrderPojo orderPojo= orderService.get(p.getOrderId());
        if(orderPojo.getInvoiceStatus()==1) {
            throw new ApiException("Can't Add: Invoice is generated");
        }
        ProductPojo productPojo=productService.get(form.getBarcode());
        if(productPojo==null){
            throw new ApiException("Sorry, "+ form.getBarcode()+" do not exist.");
        }

        p.setProductId(productPojo.getId());
        if (p.getSellingPrice() > productPojo.getMrp()) {
            throw new ApiException("Selling Price is greater than Mrp: " + productPojo.getMrp());
        }

        service.add(p,productPojo);
        inventoryService.decreaseInventory(inventoryService.get(p.getProductId()), p.getQuantity());

    }
    public void delete_id(int id) throws ApiException {
        OrderItemPojo p=service.get_id(id);

        OrderPojo orderPojo= orderService.get(p.getOrderId());
        if(orderPojo.getInvoiceStatus()==1) {
            throw new ApiException("Can't Delete: Invoice is generated");
        }
        InventoryPojo inventoryPojo=inventoryService.get(p.getProductId());
        service.delete_id(id);
        inventoryService.increaseInventory(inventoryPojo,p.getQuantity());
    }

    public List<OrderItemData> get(int orderId){
        List<OrderItemData> list1= new ArrayList<OrderItemData>();
        List<OrderItemPojo> list2= service.get(orderId);

        for(OrderItemPojo pojo: list2){
            OrderItemData data=orderItemConvert(pojo);
            ProductPojo productPojo=productService.get(pojo.getProductId());
            data.setBarcode(productPojo.getBarcode());
            list1.add(data);
        }

        return list1;

    }

    public OrderItemData get_id(int id){
        OrderItemData data=orderItemConvert(service.get_id(id));
        ProductPojo productPojo=productService.get(data.getProductId());
        data.setBarcode(productPojo.getBarcode());
         return data;
    }

//    public List<OrderItemData> getAll(){
//        List<OrderItemData> list1= new ArrayList<OrderItemData>();
//        List<OrderItemPojo> list2= service.getAll();
//        for(OrderItemPojo pojo: list2){
//            OrderItemData data=orderItemConvert(pojo);
//            ProductPojo productPojo=productService.get(pojo.getProductId());
//            data.setBarcode(productPojo.getBarcode());
//            list1.add(data);
//        }
//        return list1;
//    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id,OrderItemForm form) throws ApiException{

        OrderItemPojo p=orderItemConvert(form);
        OrderPojo orderPojo= orderService.get(p.getOrderId());
        ProductPojo productPojo= productService.get(form.getBarcode());
        if(orderPojo.getInvoiceStatus()==1) {
            throw new ApiException("Can't Edit: Invoice is generated");
        }

        if(productPojo==null){
            throw new ApiException("Sorry, "+ form.getBarcode()+" do not exist.");
        }
        if(p.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Selling Price is greater than Mrp: "+productPojo.getMrp());
        }

        p.setProductId(productPojo.getId());

        OrderItemPojo updatedPojo=service.get_id(id);

        service.update(updatedPojo,p);
        InventoryPojo inventoryPojo=inventoryService.get(p.getProductId());
        inventoryService.increaseInventory(inventoryPojo,updatedPojo.getQuantity());
        inventoryService.decreaseInventory(inventoryPojo,p.getQuantity());

    }




}


