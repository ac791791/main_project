package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.employee.util.Constants.pageRows;
import static org.junit.Assert.assertEquals;

public class BrandDtoTest extends AbstractUnitTest{

    @Autowired
    private BrandDto dto;

    private final String brand="amul";
    private final String category="dairy";
    @Before
    public void setUp() throws ApiException {
        BrandForm form= new BrandForm();
        form.setBrand(brand);
        form.setCategory(category);
        dto.add(form);
    }

    @Test
    public void testAdd() throws ApiException {
        BrandForm form= new BrandForm();
        form.setBrand("brand");
        form.setCategory("category");
        dto.add(form);
    }



    @Test
    public void testGetAll() throws ApiException {

        List<BrandData> result= dto.getAll();
        int size= result.size();
        assertEquals(1,size);

        BrandForm form2= new BrandForm();
        form2.setBrand("b2");
        form2.setCategory("c2");
        dto.add(form2);

        List<BrandData> result2=dto.getAll();
        int size2=result2.size();
        assertEquals(2,size2);

    }
    @Test
    public void testGet(){
        List<BrandData> list=dto.getAll();
        for(BrandData data:list){
            BrandData getData=dto.get(data.getId());
            assertEquals(data.getBrand(),getData.getBrand());
            assertEquals(data.getCategory(),getData.getCategory());

        }
    }

    @Test
    public void testUpdate() throws ApiException {
        List<BrandData> list=dto.getAll();
        for (BrandData data:list){
            BrandForm form=new BrandForm();
            form.setBrand("new_brand");
            form.setCategory("new_category");
            dto.update(data.getId(),form);

            BrandData getData= dto.get(data.getId());
            assertEquals("new_brand",getData.getBrand());
            assertEquals("new_category",getData.getCategory());
        }
    }
    @Test
    public void testTotalBrands(){
        int total= dto.totalBrands();
        assertEquals(1,total);
    }

    @Test
    public void testGetLimited() throws ApiException {
        for(int i=0;i<15;i++){
            BrandForm form=new BrandForm();
            form.setBrand("Brand"+i);
            form.setCategory("Category"+i);
            dto.add(form);
        }
        List<BrandData> list1=dto.getLimited(1);
        List<BrandData> list2=dto.getLimited(2);
        assertEquals(pageRows,list1.size());
        assertEquals(16-pageRows,list2.size());
    }


//    @Test
//    public void testNormalize(){
//        BrandPojo pojo= new BrandPojo();
//        pojo.setBrand("Brand");
//        pojo.setCategory("Category");
//        dto.normalize(pojo);
//        assertEquals("brand",pojo.getBrand());
//        assertEquals("category",pojo.getCategory());
//    }
//
//    @Test
//    public void testPojoToDataCovert(){
//        BrandPojo p= new BrandPojo();
//        p.setBrand("brand");
//        p.setCategory("category");
//
//        BrandData data= dto.convert(p);
//        assertEquals("brand",data.getBrand());
//        assertEquals("category",data.getCategory());
//    }
//
//    @Test
//    public void testFormToPojoConvert(){
//        BrandForm f= new BrandForm();
//        f.setBrand("brand");
//        f.setCategory("category");
//
//        BrandPojo p= dto.convert(f);
//        assertEquals("brand",p.getBrand());
//        assertEquals("category",p.getCategory());
//    }
}

