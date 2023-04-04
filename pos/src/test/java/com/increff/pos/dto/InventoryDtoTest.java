package com.increff.pos.dto;

import com.increff.pos.model.InventoryAddForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryUpdateForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.util.Constants.pageSize;
import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest{

    @Autowired
    private InventoryDto dto;
    @Autowired
    private ProductDto productDto;

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    private final String barcode="b1000";
    private int brandCategory=0;
    private final String name="curd";
    private final double mrp=25.12;

    @Before
    public void setUp() throws ApiException {
        BrandPojo brandPojo=new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);

        BrandPojo p= brandService.getByBrandCategory("brand","category");
        brandCategory=p.getId();


        ProductForm form = new ProductForm();
        form.setBarcode(barcode);
        form.setbrandCategory(brandCategory);
        form.setName(name);
        form.setMrp(mrp);
        productDto.add(form);
    }

    @Test
    public void testGetAll() throws ApiException {
        List<InventoryData> list=dto.getAll();
        int size= list.size();
        assertEquals(1,size);

        ProductPojo productPojo=new ProductPojo();
        productPojo.setBarcode("b2");
        productPojo.setbrandCategory(1);
        productPojo.setName("name2");
        productPojo.setMrp(100.05);

        productService.add(productPojo);

        List<InventoryData> list2=dto.getAll();
        int size2= list2.size();
//        assertEquals(2,size2);
    }

    @Test
    public void testGet(){
        List<InventoryData> list=dto.getAll();

        for(InventoryData data:list){
            InventoryData getData=dto.get(data.getId());
            assertEquals(0,getData.getQuantity());
        }
    }
    @Test
    public void testGetLimited() throws ApiException {
        for(int i=0;i<15;i++){
            ProductForm form = new ProductForm();
            form.setBarcode("b"+i);
            form.setbrandCategory(brandCategory);
            form.setName("Name"+i);
            form.setMrp(50+i);
            productDto.add(form);
        }
        List<InventoryData> list1=dto.getLimited(1);
        List<InventoryData> list2=dto.getLimited(2);

        assertEquals(pageSize,list1.size());
        assertEquals(16-pageSize,list2.size());
    }

    @Test
    public void testTotalInventory() throws ApiException {
        int total=dto.totalInventory();
        assertEquals(1,total);

        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(brandCategory);
        form.setName("Name");
        form.setMrp(50);
        productDto.add(form);

        total= dto.totalInventory();
        assertEquals(2,total);


    }

    @Test
    public void testUpdate() throws ApiException {
        List<InventoryData> list=dto.getAll();
        for(InventoryData data:list){
            InventoryUpdateForm form=new InventoryUpdateForm();
            form.setQuantity(10);
            dto.update(data.getId(), form);

            InventoryData getData=dto.get(data.getId());
            assertEquals(10,getData.getQuantity());
        }

    }

    @Test
    public void testTopUpdate() throws ApiException {
        List<InventoryData> list=dto.getAll();
        for(InventoryData data:list){
            InventoryAddForm form=new InventoryAddForm();
            form.setQuantity(10);
            form.setBarcode(barcode);
            dto.topUpdate(form);

            InventoryData getData=dto.get(data.getId());
            assertEquals(10,getData.getQuantity());
        }
    }

    @Test
    public void testTopUpdateWithoutBarcode(){
        List<InventoryData> list=dto.getAll();
        try {
            for(InventoryData data:list){
                InventoryAddForm form=new InventoryAddForm();
                form.setQuantity(10);
                dto.topUpdate(form);

                InventoryData getData=dto.get(data.getId());
                assertEquals(10,getData.getQuantity());
            }

        }catch (ApiException e){
            assertEquals("Barcode can't be empty",e.getMessage());
        }
    }

    @Test
    public void testTopUpdateDifferentBarcode(){
        List<InventoryData> list=dto.getAll();
        try {
            for(InventoryData data:list){
                InventoryAddForm form=new InventoryAddForm();
                form.setQuantity(10);
                form.setBarcode("differentBarcode");
                dto.topUpdate(form);

                InventoryData getData=dto.get(data.getId());
                assertEquals(10,getData.getQuantity());
            }

        }catch (ApiException e){
            assertEquals("Sorry, differentBarcode is not present.",e.getMessage());
        }
    }

}
