package com.increff.pos.service;

import com.increff.pos.dto.AbstractUnitTest;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.util.Constants.pageRows;
import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends AbstractUnitTest {

    @Autowired
    private ProductService service;
    @Autowired
    private InventoryService inventoryService;

    private final String barcode="b1000";
    private final int brandCategory=1;
    private final String name="curd";
    private final double mrp=25.12;

    @Before
    public void setUp() throws ApiException {


        ProductPojo p = new ProductPojo();
        p.setBarcode(barcode);
        p.setbrandCategory(brandCategory);
        p.setName(name);
        p.setMrp(mrp);
        service.add(p);


    }

    @Test
    public void testAdd() throws ApiException {

        ProductPojo p = new ProductPojo();
        p.setBarcode("barcode");
        p.setbrandCategory(2);
        p.setName("name");
        p.setMrp(50.25);
        service.add(p);
    }



    @Test
    public void testDuplicateBarcodeAdd() throws ApiException {
        try {
            ProductPojo p = new ProductPojo();
            p.setBarcode(barcode);
            p.setbrandCategory(1);
            p.setName("name");
            p.setMrp(50.25);
            service.add(p);

        }catch (ApiException e){
            assertEquals("Couldn't Add: Given Barcode already exist",e.getMessage());
        }
    }


    @Test
    public void testGetAll() throws ApiException {
        List<ProductPojo> list=service.getAll();
        int size= list.size();

        assertEquals(1,size);

        ProductPojo p = new ProductPojo();
        p.setBarcode("barcode");
        p.setbrandCategory(2);
        p.setName("name");
        p.setMrp(50.25);
        service.add(p);

        List<ProductPojo> list2=service.getAll();
        int size2= list2.size();

        assertEquals(2,size2);
    }


    @Test
    public void testGetId(){
        List<ProductPojo> list=service.getAll();

        for(ProductPojo pojo: list){
            ProductPojo p=service.get(pojo.getId());
            assertEquals(barcode,p.getBarcode());
            assertEquals(brandCategory,p.getbrandCategory());
            assertEquals(name,p.getName());
            assertEquals(mrp,p.getMrp(),0.01);
        }
    }

    @Test
    public void testGetBarcode(){
        List<ProductPojo> list=service.getAll();

        for(ProductPojo pojo: list){
            ProductPojo p=service.get(pojo.getBarcode());
            assertEquals(barcode,p.getBarcode());
            assertEquals(brandCategory,p.getbrandCategory());
            assertEquals(name,p.getName());
            assertEquals(mrp,p.getMrp(),0.01);
        }
    }




    @Test
    public void testUpdate() throws ApiException {
        List<ProductPojo> list=service.getAll();

        for (ProductPojo pojo: list){
            ProductPojo p= new ProductPojo();
            p.setBarcode(pojo.getBarcode());
            p.setbrandCategory(pojo.getbrandCategory());
            p.setName("puma");
            p.setMrp(100.08);

            service.update(pojo.getId(), p);

            ProductPojo newPojo= service.get(pojo.getId());
            assertEquals("puma",newPojo.getName());
            assertEquals(100.08,newPojo.getMrp(),0.01);
        }
    }

    @Test
    public void testTotalProducts() throws ApiException {
        int total= service.totalProducts();
        assertEquals(1,total);

        ProductPojo p = new ProductPojo();
        p.setBarcode("barcode");
        p.setbrandCategory(2);
        p.setName("name");
        p.setMrp(50.25);
        service.add(p);

        total= service.totalProducts();
        assertEquals(2,total);

    }

    @Test
    public void testGetLimited() throws ApiException {
        for (int i = 0; i < 15; i++) {
            ProductPojo p = new ProductPojo();
            p.setBarcode("barcode" + i);
            p.setbrandCategory(2);
            p.setName("name" + i);
            p.setMrp(50.25 + i);
            service.add(p);
        }
        assertEquals(pageRows, service.getLimited(1).size());
        assertEquals(16-pageRows,service.getLimited(2).size());

    }


}