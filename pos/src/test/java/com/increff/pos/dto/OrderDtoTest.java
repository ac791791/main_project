package com.increff.pos.dto;

import com.increff.pos.model.InventoryAddForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.Constants.pageSize;
import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto dto;
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

        dto.add(itemList);
    }

    @Test
    public void testAdd() throws ApiException {
        List<OrderForm> itemList= new ArrayList<OrderForm>();
        OrderForm orderForm = new OrderForm();
        orderForm.setBarcode("b1");
        orderForm.setQuantity(quantity);
        orderForm.setSellingPrice(sellingPrice);
        itemList.add(orderForm);

        OrderForm orderForm2 = new OrderForm();
        orderForm2.setBarcode("b2");
        orderForm2.setQuantity(quantity);
        orderForm2.setSellingPrice(sellingPrice);
        itemList.add(orderForm2);

        dto.add(itemList);
    }

    @Test
    public void testAddWithoutItems(){
        List<OrderForm> itemList= new ArrayList<OrderForm>();

        try {
            dto.add(itemList);
        } catch (ApiException e) {
            assertEquals("Can't Add: No orderItem present ",e.getMessage());
        }
    }

    @Test
    public void testCheckOrderForm() throws ApiException {
        OrderForm orderForm = new OrderForm();
        orderForm.setBarcode("b1");
        orderForm.setSellingPrice(10);
        orderForm.setQuantity(10);

        dto.checkOrderForm(orderForm);
    }
    @Test
    public void testGetAll() throws ApiException {
        List<OrderData> list1 = dto.getAll();
        assertEquals(1, list1.size());

        List<OrderForm> itemList= new ArrayList<OrderForm>();
        OrderForm orderForm = new OrderForm();
        orderForm.setBarcode("b1");
        orderForm.setQuantity(quantity);
        orderForm.setSellingPrice(sellingPrice);
        itemList.add(orderForm);
        dto.add(itemList);

        List<OrderData> list2 = dto.getAll();
        assertEquals(2, list2.size());
    }

    @Test
    public void testGet() throws ApiException {
        List<OrderData> list = dto.getAll();

        for (OrderData data : list) {
            OrderData getData = dto.get(data.getId());

            assertEquals(data.getTime(), getData.getTime());
            assertEquals(data.getInvoiceStatus(), getData.getInvoiceStatus());
        }
    }

    @Test
    public void testGetWrongId() throws ApiException {
        int id=10000;
        try{
            OrderData data = dto.get(id);
        }
        catch (ApiException e){
            assertEquals("Order with given id "+id+" does not exist", e.getMessage());
        }

    }


    @Test
    public void testGetLimited() throws ApiException {
        for (int i = 0; i < 15; i++) {
            List<OrderForm> itemList= new ArrayList<OrderForm>();
            OrderForm orderForm = new OrderForm();
            orderForm.setBarcode("b1");
            orderForm.setQuantity(1);
            orderForm.setSellingPrice(sellingPrice);
            itemList.add(orderForm);
            dto.add(itemList);
        }
        assertEquals(pageSize, dto.getLimited(1).size());
        assertEquals(16 - pageSize, dto.getLimited(2).size());
    }

    @Test
    public void testGetLimitedNegativePageNo() throws ApiException {
        for (int i = 0; i < 15; i++) {
            List<OrderForm> itemList= new ArrayList<OrderForm>();
            OrderForm orderForm = new OrderForm();
            orderForm.setBarcode("b1");
            orderForm.setQuantity(1);
            orderForm.setSellingPrice(sellingPrice);
            itemList.add(orderForm);
            dto.add(itemList);
        }

        try {
            List<OrderData> orderDataList = dto.getLimited(-1);
        }
        catch (ApiException e){
            assertEquals("Page No can't be less than 1",e.getMessage());
        }
    }

    @Test
    public void testTotalOrders() throws ApiException {
        int total = dto.totalOrders();

        assertEquals(1, total);

        List<OrderForm> itemList= new ArrayList<OrderForm>();
        OrderForm orderForm = new OrderForm();
        orderForm.setBarcode("b1");
        orderForm.setQuantity(1);
        orderForm.setSellingPrice(sellingPrice);
        itemList.add(orderForm);
        dto.add(itemList);

        total = dto.totalOrders();
        assertEquals(2, total);
    }

    @Test
    public void testDelete() throws ApiException {
        List<OrderData> list = dto.getAll();

        for (OrderData data : list) {
            dto.delete(data.getId());
        }
        List<OrderData> list1 = dto.getAll();
        assertEquals(0, list1.size());
    }

    @Test
    public void testNotZeroInvoiceStatus() throws ApiException {
        List<OrderData> list = dto.getAll();
        for (OrderData data : list) {
            orderService.changeInvoiceStatus(data.getId());
        }
        try {
            for (OrderData data : list) {
                dto.delete(data.getId());
            }
            List<OrderData> list1 = dto.getAll();
            assertEquals(0, list1.size());
        }
        catch (ApiException e){
            assertEquals("Can't Delete: Invoice is generated",e.getMessage());
        }


    }
}
