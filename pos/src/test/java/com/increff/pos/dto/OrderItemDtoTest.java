package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    private int brandCategory=0;

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

        InventoryForm inventoryForm=new InventoryForm();
        inventoryForm.setBarcode("b1");
        inventoryForm.setQuantity(100);

        inventoryDto.topUpdate(inventoryForm);


        orderDto.addOrder();
        List<OrderData> orderDataList=orderDto.getAll();

        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();
            form.setOrderId(orderData.getId());
            form.setBarcode("b1");
            form.setQuantity(10);
            form.setSellingPrice(100);
            dto.add(form);
        }
    }


    @Test
    public void testAdd() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        for(OrderData orderData: orderDataList){
            OrderItemForm form= new OrderItemForm();
            form.setOrderId(orderData.getId());
            form.setBarcode("b1");
            form.setQuantity(10);
            form.setSellingPrice(100);
            dto.add(form);
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
            assertEquals("Sorry, differentBarcode do not exist.",e.getMessage());
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
                form.setBarcode("b1");
                form.setQuantity(10);
                form.setSellingPrice(100);
                dto.add(form);
            }
        }
        catch (ApiException e){
            assertEquals("Can't Add: Invoice is generated",e.getMessage());
        }
    }

    @Test
    public void testSellingPriceGreaterThanMrpAdd() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();

        try {
            for (OrderData orderData : orderDataList) {
                OrderItemForm form = new OrderItemForm();
                form.setOrderId(orderData.getId());
                form.setBarcode("b1");
                form.setQuantity(10);
                form.setSellingPrice(200);
                dto.add(form);
            }
        }
        catch (ApiException e){
            assertEquals("Selling Price is greater than Mrp: 100.0" ,e.getMessage());
        }
    }


    @Test
    public void testGetByOrderId(){
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData orderData : orderDataList) {
           List<OrderItemData> orderItemDataList=dto.get(orderData.getId());

           for(OrderItemData data: orderItemDataList){
               assertEquals("b1",data.getBarcode());
               assertEquals(10,data.getQuantity());
               assertEquals(100.00,data.getSellingPrice(),0.01);
           }
        }
    }


    @Test
    public void testGetByOrderItemId(){
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData orderData : orderDataList) {
            List<OrderItemData> orderItemDataList=dto.get(orderData.getId());

            for(OrderItemData data: orderItemDataList){
                OrderItemData getData=dto.get_id(data.getId());
                assertEquals("b1",getData.getBarcode());
                assertEquals(10,getData.getQuantity());
                assertEquals(100.00,getData.getSellingPrice(),0.01);
            }
        }
    }

    @Test
    public void testDelete() throws ApiException {
        List<OrderData> orderDataList=orderDto.getAll();
        for (OrderData orderData : orderDataList) {
            List<OrderItemData> orderItemDataList=dto.get(orderData.getId());

            for(OrderItemData data: orderItemDataList){
                dto.delete_id(data.getId());
            }
            List<OrderItemData> orderItemDataList1=dto.get(orderData.getId());
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
            List<OrderItemData> orderItemDataList=dto.get(orderData.getId());

            for(OrderItemData data: orderItemDataList){
                try {
                    dto.delete_id(data.getId());
                }
                catch (ApiException e){
                    assertEquals("Can't Delete: Invoice is generated",e.getMessage());
                }

            }
            List<OrderItemData> orderItemDataList1=dto.get(orderData.getId());
            assertEquals(1,orderItemDataList1.size());
        }

    }


}
