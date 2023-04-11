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

public class ReportDtoTest extends AbstractUnitTest{

    @Autowired
    private ReportDto dto;
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
    private double sellingPrice = 50;

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
    public void testBrandReport(){
        BrandReportForm form = new BrandReportForm();
        form.setBrand("");
        form.setCategory("");
        List<BrandReportData> brandReportDataList = dto.getBrandReport(form);

        assertEquals(1,brandReportDataList.size());

        form.setBrand("differentBrand");
        List<BrandReportData> brandReportDataList2 = dto.getBrandReport(form);
        assertEquals(0,brandReportDataList2.size());
    }

    @Test
    public void testInventoryReport(){
        InventoryReportForm form = new InventoryReportForm();
        form.setBrand("");
        form.setCategory("");
        List<InventoryReportData> inventoryReportDataList = dto.getInventoryReport(form);

        assertEquals(2,inventoryReportDataList.size());

        form.setBrand("differentBrand");
        List<InventoryReportData> inventoryReportDataList1 = dto.getInventoryReport(form);
        assertEquals(0,inventoryReportDataList1.size());
    }

    @Test
    public void testSalesReportWithoutDates() throws ApiException {
        SalesReportForm form = new SalesReportForm();

        List<SalesReportData> salesReportDataList = dto.getSalesReport(form);
        assertEquals(1,salesReportDataList.size());

        form.setBrand("differentBrand");
        List<SalesReportData> salesReportDataList1 = dto.getSalesReport(form);
        assertEquals(0,salesReportDataList1.size());

    }
    @Test
    public void testSalesReportWithDates() throws ApiException {
        SalesReportForm form = new SalesReportForm();
        form.setStartDate("2023-02-26");
        form.setEndDate("2023-04-06");
        List<SalesReportData> salesReportDataList = dto.getSalesReport(form);
        assertEquals(1,salesReportDataList.size());

        form.setBrand("differentBrand");
        List<SalesReportData> salesReportDataList1 = dto.getSalesReport(form);
        assertEquals(0,salesReportDataList1.size());
    }

    @Test
    public void testSalesReportStartDateGreaterThanEndDate() throws ApiException {
        SalesReportForm form = new SalesReportForm();
        form.setStartDate("2023-04-06");
        form.setEndDate("2023-02-26");
        try{
            List<SalesReportData> salesReportDataList = dto.getSalesReport(form);
        }
        catch (ApiException e){
            assertEquals("Start date can't exceed End Date",e.getMessage());
        }
    }
    @Test
    public void testSalesReportMaximumGapGreaterThan356Days() throws ApiException {
        SalesReportForm form = new SalesReportForm();
        form.setStartDate("2021-04-06");
        form.setEndDate("2023-04-06");
        try{
            List<SalesReportData> salesReportDataList = dto.getSalesReport(form);
        }
        catch (ApiException e){
            assertEquals("Maximum gap allowed is 356 days",e.getMessage());
        }
    }

}
