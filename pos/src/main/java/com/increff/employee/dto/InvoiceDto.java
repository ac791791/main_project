package com.increff.employee.dto;


import com.increff.employee.client.InvoiceClient;
import com.increff.employee.model.InvoiceDetails;
import com.increff.employee.model.InvoiceItem;
import com.increff.employee.model.OrderData;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
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
        System.out.println("Check 2");
        getOrderItems(orderPojo.getId(),response);
    }


    public void getOrderItems(int orderId,HttpServletResponse response) throws ApiException, IOException{
        List<OrderItemPojo> orderItemPojoList= orderItemService.get(orderId);
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
