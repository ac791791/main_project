package com.increff.pos.dto;


import com.increff.pos.client.InvoiceClient;
import com.increff.pos.model.InvoiceDetails;
import com.increff.pos.model.InvoiceItem;
import com.increff.pos.model.OrderData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InvoiceDto {


    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InvoiceClient invoiceClient;
    public void generateInvoice(int orderId, HttpServletResponse response) throws ApiException, IOException {
        OrderPojo orderPojo= orderService.get(orderId);
        if(orderPojo==null)
        {
            throw new ApiException("No Order with OrderId exist");
        }
        getOrderItems(orderPojo.getId(),response);
    }


    public void getOrderItems(int orderId,HttpServletResponse response) throws ApiException, IOException{
        List<OrderItemPojo> orderItemPojoList= orderItemService.getByOrderId(orderId);
        List<InvoiceItem> invoiceItemList= new ArrayList<InvoiceItem>();

        for(OrderItemPojo pojo: orderItemPojoList){
            InvoiceItem item= new InvoiceItem();

            ProductPojo productPojo= productService.get(pojo.getProductId());
            item.setName(productPojo.getName());
            item.setQuantity(pojo.getQuantity());
            item.setPrice(pojo.getSellingPrice());

            invoiceItemList.add(item);
        }
        InvoiceDetails invoiceDetails=new InvoiceDetails();

        LocalDateTime time=orderService.get(orderId).getTime();
        invoiceDetails.setTime(time);
        invoiceDetails.setOrderId(orderId);
        invoiceDetails.setItems(invoiceItemList);

        System.out.println("Check 3");

        invoiceClient.generateInvoice(invoiceDetails,response);
        System.out.println("Check Final");

    }
}
