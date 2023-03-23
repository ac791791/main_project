package com.increff.employee.service;

import com.increff.employee.dto.AbstractUnitTest;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.employee.util.Constants.pageRows;
import static org.junit.Assert.assertEquals;

public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService service;

    private final String brand="amul";
    private final String category="dairy";

    @Before
    public void setUp() throws ApiException {
        BrandPojo p=new BrandPojo();
        p.setBrand(brand);
        p.setCategory(category);
        service.add(p);
    }

    @Test
    public void testAdd() throws ApiException {
        BrandPojo p=new BrandPojo();
        p.setBrand("brand");
        p.setCategory("category");
        service.add(p);
    }

    @Test
    public void testDuplicateAdd() throws ApiException{
        BrandPojo pojo= new BrandPojo();
        pojo.setBrand(brand);
        pojo.setCategory(category);
        try {
            service.add(pojo);
        }
        catch (ApiException e){
            assertEquals("Couldn't Add: Given Brand Category already exist",e.getMessage());
        }
    }

    @Test
    public void testGetAll() throws ApiException {
        List<BrandPojo> list=service.getAll();
        int size= list.size();
        assertEquals(1,size);

        BrandPojo p2=new BrandPojo();
        p2.setBrand("brand2");
        p2.setCategory("category2");
        service.add(p2);

        List<BrandPojo> list2=service.getAll();
        int size2=list2.size();
        assertEquals(2,size2);

    }

    @Test
    public void testGet(){
        List<BrandPojo> list=service.getAll();
        for(BrandPojo pojo: list){
            BrandPojo getPojo= service.get(pojo.getId());
            assertEquals(pojo.getBrand(),getPojo.getBrand());
            assertEquals(pojo.getCategory(),getPojo.getCategory());

        }
    }

    @Test
    public void testGetByBrandCategory(){
        BrandPojo p=service.getByBrandCategory(brand,category);
        List<BrandPojo> list=service.getAll();

        for(BrandPojo pojo:list){
            assertEquals(p,pojo);
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        List<BrandPojo> list=service.getAll();
        for(BrandPojo pojo: list){
            BrandPojo newPojo= new BrandPojo();
            newPojo.setBrand("new_brand");
            newPojo.setCategory("new_category");
            service.update(pojo.getId(), newPojo);

            BrandPojo getPojo= service.get(pojo.getId());
            assertEquals("new_brand",newPojo.getBrand());
            assertEquals("new_category",newPojo.getCategory());
        }

    }

    @Test
    public void testSameBrandCategoryUpdate() throws ApiException {
        List<BrandPojo> list=service.getAll();
        for(BrandPojo pojo: list){
            BrandPojo newPojo= new BrandPojo();
            newPojo.setBrand(brand);
            newPojo.setCategory(category);
            service.update(pojo.getId(), newPojo);

            BrandPojo getPojo= service.get(pojo.getId());
            assertEquals(brand,newPojo.getBrand());
            assertEquals(category,newPojo.getCategory());
        }

    }
    @Test
    public void testDuplicateBrandCategoryUpdate() throws ApiException {

        BrandPojo p=new BrandPojo();
        p.setBrand("brand");
        p.setCategory("category");
        service.add(p);

        List<BrandPojo> list=service.getAll();
        try {
            for (BrandPojo pojo : list) {
                BrandPojo newPojo = new BrandPojo();
                newPojo.setBrand(brand);
                newPojo.setCategory(category);
                service.update(pojo.getId(), newPojo);

                BrandPojo getPojo = service.get(pojo.getId());
                assertEquals(brand, newPojo.getBrand());
                assertEquals(category, newPojo.getCategory());
            }
        }
        catch (ApiException e){
            assertEquals("Couldn't Update: Given Brand Category already exist",e.getMessage());
        }

    }

    @Test
    public void testTotalBrands() throws ApiException {
        int total=service.totalBrands();
        assertEquals(1,total);

        BrandPojo p= new BrandPojo();
        p.setBrand("Brand");
        p.setCategory("Category");
        service.add(p);

        total= service.totalBrands();
        assertEquals(2,total);

    }

    @Test
    public void testGetLimited() throws ApiException {
        for(int i=0;i<15;i++){
            BrandPojo p=new BrandPojo();
            p.setBrand("Brand"+i);
            p.setCategory("Category"+i);
            service.add(p);
        }
        List<BrandPojo> list1=service.getLimited(1);
        List<BrandPojo> list2=service.getLimited(2);
        assertEquals(pageRows,list1.size());
        assertEquals(16-pageRows,list2.size());
    }


}
