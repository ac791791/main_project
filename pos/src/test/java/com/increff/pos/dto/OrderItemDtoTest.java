package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemDtoTest extends AbstractUnitTest{

    @Autowired
    private OrderItemDto dto;

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OrderService orderService;

    private int brandCategory = 0;
    private int quantity = 10;
    private double sellingPrice = 10.55;

    @Before
    public void setUp() throws ApiException {

        BrandPojo brandPojo=new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);

        BrandPojo p= brandService.getByBrandCategory("brand","category");
        brandCategory=p.getId();


        ProductForm productForm = new ProductForm();
        productForm.setBarcode("b1");
        productForm.setbrandCategory(brandCategory);
        productForm.setName("name1");
        productForm.setMrp(100);
        productDto.add(productForm);

        InventoryAddForm inventoryForm=new InventoryAddForm();
        inventoryForm.setBarcode("b1");
        inventoryForm.setQuantity(100);
        inventoryDto.topUpdate(inventoryForm);


        ProductForm productForm2 = new ProductForm();
        productForm2.setBarcode("b2");
        productForm2.setbrandCategory(brandCategory);
        productForm2.setName("name2");
        productForm2.setMrp(200);
        productDto.add(productForm2);

        InventoryAddForm inventoryForm2=new InventoryAddForm();
        inventoryForm2.setBarcode("b2");
        inventoryForm2.setQuantity(100);
        inventoryDto.topUpdate(inventoryForm2);



        List<OrderForm> itemList= new ArrayList<OrderForm>();
        OrderForm orderForm = new OrderForm();
        orderForm.setBarcode("b1");
        orderForm.setQuantity(quantity);
        orderForm.setSellingPrice(sellingPrice);
        itemList.add(orderForm);

        orderDto.add(itemList);
    }


    @Test
    public void testAdd() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();
            form.setOrderId(orderData.getId());
            form.setBarcode("b2");
            form.setQuantity(10);
            form.setSellingPrice(100);
            dto.add(form);
        }
    }

    @Test
    public void testLessInventoryAdd() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();
            form.setOrderId(orderData.getId());
            form.setBarcode("b2");
            form.setQuantity(1000);
            form.setSellingPrice(100);
            try {
                dto.add(form);
            }
            catch (ApiException e){
                assertEquals("This much quantity is not present. Max Quantity: 100",e.getMessage());
            }

        }
    }


    @Test
    public void testDifferentBarcodeAdd() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        try {
            for (OrderData orderData : orderDataList) {
                OrderItemForm form = new OrderItemForm();
                form.setOrderId(orderData.getId());
                form.setBarcode("differentBarcode");
                form.setQuantity(10);
                form.setSellingPrice(100);
                dto.add(form);
            }
        }
        catch (ApiException e){
            assertEquals("Product with given barcode differentBarcode does not exist",e.getMessage());
        }
    }

    @Test
    public void testInvoicedOrderAdd() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData data : orderDataList) {
            orderService.changeInvoiceStatus(data.getId());
        }
        try {
            for (OrderData orderData : orderDataList) {
                OrderItemForm form = new OrderItemForm();
                form.setOrderId(orderData.getId());
                form.setBarcode("b2");
                form.setQuantity(10);
                form.setSellingPrice(100);
                dto.add(form);
            }
        }
        catch (ApiException e){
            assertEquals("Can't Change: Invoice is generated",e.getMessage());
        }
    }

    @Test
    public void testSellingPriceGreaterThanMrpAdd() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        try {
            for (OrderData orderData : orderDataList) {
                OrderItemForm form = new OrderItemForm();
                form.setOrderId(orderData.getId());
                form.setBarcode("b2");
                form.setQuantity(10);
                form.setSellingPrice(300);
                dto.add(form);
            }
        }
        catch (ApiException e){
            assertEquals("Selling Price is greater than Mrp: 200.0" ,e.getMessage());
        }
    }


    @Test
    public void testGetByOrderId() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData orderData : orderDataList) {
           List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());

           for(OrderItemData data: orderItemDataList){
               assertEquals("b1",data.getBarcode());
               assertEquals(10,data.getQuantity());
               assertEquals(10.55,data.getSellingPrice(),0.01);
           }
        }
    }


    @Test
    public void testGetByOrderItemId() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData orderData : orderDataList) {
            List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());

            for(OrderItemData data: orderItemDataList){
                OrderItemData getData=dto.getCheck(data.getId());
                assertEquals("b1",getData.getBarcode());
                assertEquals(10,getData.getQuantity());
                assertEquals(10.55,getData.getSellingPrice(),0.01);
            }
        }
    }

    @Test
    public void testDelete() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData orderData : orderDataList) {
            List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());
            for(OrderItemData data: orderItemDataList){
                dto.delete(data.getId());
            }
            List<OrderItemData> orderItemDataList1=dto.getByOrderId(orderData.getId());
            assertEquals(0,orderItemDataList1.size());
        }

    }


    @Test
    public void testInvoicedDelete() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData data : orderDataList) {
            orderService.changeInvoiceStatus(data.getId());
        }

        for (OrderData orderData : orderDataList) {
            List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());

            for(OrderItemData data: orderItemDataList){
                try {
                    dto.delete(data.getId());
                }
                catch (ApiException e){
                    assertEquals("Can't Delete: Invoice is generated",e.getMessage());
                }

            }
            List<OrderItemData> orderItemDataList1=dto.getByOrderId(orderData.getId());
            assertEquals(1,orderItemDataList1.size());
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();

            form.setOrderId(orderData.getId());
            form.setBarcode("b1");
            form.setQuantity(5);
            form.setSellingPrice(50.12);

            List<OrderItemData> itemDataList=dto.getByOrderId(orderData.getId());
            for(OrderItemData itemData: itemDataList) {
                dto.update(itemData.getId(), form);
            }

            List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());

            for(OrderItemData orderItemData: orderItemDataList){
                assertEquals(5,orderItemData.getQuantity());
                assertEquals(50.12,orderItemData.getSellingPrice(),0.01);
            }
        }
    }

    @Test
    public void testInvoicedUpdate() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData data : orderDataList) {
            orderService.changeInvoiceStatus(data.getId());
        }
        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();

            form.setOrderId(orderData.getId());
            form.setBarcode("b1");
            form.setQuantity(5);
            form.setSellingPrice(50.12);
            try {
                List<OrderItemData> itemDataList=dto.getByOrderId(orderData.getId());
                for(OrderItemData itemData: itemDataList) {
                    dto.update(itemData.getId(), form);
                }
            }
            catch (ApiException e){
                assertEquals("Can't Change: Invoice is generated",e.getMessage());
            }


            List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());

            for(OrderItemData orderItemData: orderItemDataList){
                assertEquals(10,orderItemData.getQuantity());
                assertEquals(10.55,orderItemData.getSellingPrice(),0.01);
            }
        }
    }


    @Test
    public void testDifferentBarcodeUpdate() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();

            form.setOrderId(orderData.getId());
            form.setBarcode("differentBarcode");
            form.setQuantity(5);
            form.setSellingPrice(50.12);
            try {
                List<OrderItemData> itemDataList=dto.getByOrderId(orderData.getId());
                for(OrderItemData itemData: itemDataList) {
                    dto.update(itemData.getId(), form);
                }
            }
            catch (ApiException e){
                assertEquals("Product with given barcode differentBarcode does not exist",e.getMessage());
            }


            List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());

            for(OrderItemData orderItemData: orderItemDataList){
                assertEquals(10,orderItemData.getQuantity());
                assertEquals(10.55,orderItemData.getSellingPrice(),0.01);
            }
        }
    }


    @Test
    public void testSellingPriceGreaterThanMrpUpdate() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();

            form.setOrderId(orderData.getId());
            form.setBarcode("b1");
            form.setQuantity(5);
            form.setSellingPrice(101);
            try {
                dto.update(orderData.getId(), form);
            }
            catch (ApiException e){
                assertEquals("Selling Price is greater than Mrp: 100.0" ,e.getMessage());
            }


            List<OrderItemData> orderItemDataList=dto.getByOrderId(orderData.getId());

            for(OrderItemData orderItemData: orderItemDataList){
                assertEquals(10,orderItemData.getQuantity());
                assertEquals(10.55,orderItemData.getSellingPrice(),0.01);
            }
        }
    }
}
