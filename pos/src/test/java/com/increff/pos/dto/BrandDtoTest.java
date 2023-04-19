package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.util.Constants.maxStringLength;
import static com.increff.pos.util.Constants.pageSize;
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
    public void testNullBrandAdd() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand(null);
        form.setCategory("category");
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Brand can't be null/empty. ",e.getMessage());
        }
    }

    @Test
    public void testEmptyBrandAdd() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("");
        form.setCategory("category");
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Brand can't be null/empty. ",e.getMessage());
        }
    }

    @Test
    public void testNullCategoryAdd() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("brand");
        form.setCategory(null);
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Category can't be null/empty. ",e.getMessage());
        }
    }

    @Test
    public void testEmptyCategoryAdd() throws ApiException{
        BrandForm form= new BrandForm();
        form.setBrand("brand");
        form.setCategory("");
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Category can't be null/empty. ",e.getMessage());
        }
    }

    @Test
    public void testGreaterBrandLengthAdd(){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("abcdefghijkabcdefghijkabcdefghijkabcdefghijkabcdefghijk");
        brandForm.setCategory("categoty");

        try {
            dto.add(brandForm);
        }
        catch (ApiException e){
            assertEquals("Length of brand can't exceed "+maxStringLength,e.getMessage());
        }
    }

    @Test
    public void testGreaterCategoryLengthAdd(){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand");
        brandForm.setCategory("abcdefghijkabcdefghijkabcdefghijkabcdefghijkabcdefghijk");

        try {
            dto.add(brandForm);
        }
        catch (ApiException e){
            assertEquals("Length of category can't exceed "+maxStringLength,e.getMessage());
        }
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
    public void testGetCheck() throws ApiException {
        List<BrandData> list=dto.getAll();
        for(BrandData data:list){
            BrandData getData=dto.getCheck(data.getId());
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

            BrandData getData= dto.getCheck(data.getId());
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
        assertEquals(pageSize,list1.size());
        assertEquals(16-pageSize,list2.size());
    }

    @Test
    public void testGetLimitedNegativePageNo() throws ApiException {
        for(int i=0;i<15;i++){
            BrandForm form=new BrandForm();
            form.setBrand("Brand"+i);
            form.setCategory("Category"+i);
            dto.add(form);
        }
        try {
            List<BrandData> list1=dto.getLimited(-1);
        }
        catch (ApiException e){
            assertEquals("Page No can't be less than 1",e.getMessage());
        }

    }

}

