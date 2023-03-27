package com.increff.pos.dto;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.util.Constants.pageRows;
import static com.increff.pos.util.NormalizeFunctions.normalize;
import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest{

    @Autowired
    private ProductDto dto;
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
        dto.add(form);
    }

    @Test
    public void testAdd() throws ApiException {
        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(brandCategory);
        form.setName("Name");
        form.setMrp(50);
        dto.add(form);
    }

    @Test
    public void testEmptyBarcodeAdd() throws ApiException{
        ProductForm form = new ProductForm();
        form.setbrandCategory(brandCategory);
        form.setName("Name");
        form.setMrp(50);
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Barcode can't be empty",e.getMessage());
        }

    }

    @Test
    public void testZeroBrandCategoryAdd() throws ApiException{
        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(0);
        form.setName("Name");
        form.setMrp(50);
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Please Select Brand/Category Option",e.getMessage());
        }

    }

    @Test
    public void testEmptyNameAdd() throws ApiException{
        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(brandCategory);
        form.setName("");
        form.setMrp(50);
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Name can't be empty",e.getMessage());
        }

    }

    @Test
    public void testNegativeMrpAdd() throws ApiException{
        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(brandCategory);
        form.setName("name");
        form.setMrp(-1);
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Mrp can't be lesser than 0",e.getMessage());
        }

    }

    @Test
    public void testMuchGreaterMrpAdd() throws ApiException{
        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(brandCategory);
        form.setName("name");
        form.setMrp(1000000000);
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Mrp can't be greater than 999999",e.getMessage());
        }

    }

    @Test
    public void testDifferentBrandCategoryAdd() throws ApiException{
        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(brandCategory+1);
        form.setName("name");
        form.setMrp(10);
        try {
            dto.add(form);
        }
        catch (ApiException e){
            assertEquals("Brand Category is not found for this product",e.getMessage());
        }

    }


    @Test
    public void testGetAll() throws ApiException {
        List<ProductData> list=dto.getAll();
        int size= list.size();
        assertEquals(1,size);

       ProductForm form2 = new ProductForm();
        form2.setBarcode("b2000");
        form2.setbrandCategory(brandCategory);
        form2.setName("Name");
        form2.setMrp(50);
        dto.add(form2);

        List<ProductData> list2=dto.getAll();
        int size2= list2.size();
        assertEquals(2,size2);
    }

    @Test
    public void testGet(){
        List<ProductData> list=dto.getAll();
        for(ProductData data: list){
            ProductData newData= dto.get(data.getId());
            assertEquals(barcode,newData.getBarcode());
            assertEquals(brandCategory,newData.getbrandCategory());
            assertEquals(name,newData.getName());
            assertEquals(mrp,newData.getMrp(),0.01);
        }
    }

    @Test
    public void testTotalProducts() throws ApiException {
        int total=dto.totalProducts();
        assertEquals(1,total);

        ProductForm form = new ProductForm();
        form.setBarcode("b2000");
        form.setbrandCategory(brandCategory);
        form.setName("Name");
        form.setMrp(50);
        dto.add(form);

        total= dto.totalProducts();
        assertEquals(2,total);


    }
    @Test
    public void testGetLimited() throws ApiException {
        for(int i=0;i<15;i++){
            ProductForm form = new ProductForm();
            form.setBarcode("b"+i);
            form.setbrandCategory(brandCategory);
            form.setName("Name"+i);
            form.setMrp(50+i);
            dto.add(form);
        }
        List<ProductData> list1=dto.getLimited(1);
        List<ProductData> list2=dto.getLimited(2);

        assertEquals(pageRows,list1.size());
        assertEquals(16-pageRows,list2.size());
    }

    @Test
    public void testUpdate() throws ApiException {
        List<ProductData> list=dto.getAll();
        for(ProductData data:list){
            ProductForm form = new ProductForm();
            form.setName("Name");
            form.setMrp(50.22);
            dto.update(data.getId(),form);

            ProductData getData=dto.get(data.getId());
            assertEquals("name",getData.getName());
            assertEquals(50.22,getData.getMrp(),0.01);
        }
    }

//    @Test
//    public void testFormToPojoConvert(){
//        ProductForm form = new ProductForm();
//        form.setBarcode(barcode);
//        form.setbrandCategory(brandCategory);
//        form.setName(name);
//        form.setMrp(mrp);
//
//        ProductPojo pojo=dto.convert(form);
//        assertEquals(barcode,pojo.getBarcode());
//        assertEquals(brandCategory,pojo.getbrandCategory());
//        assertEquals(name,pojo.getName());
//        assertEquals(mrp,pojo.getMrp(),0.01);
//
//
//    }
//
//    @Test
//    public void testPojoToDataConvert(){
//        ProductPojo pojo=new ProductPojo();
//        pojo.setBarcode(barcode);
//        pojo.setbrandCategory(brandCategory);
//        pojo.setName(name);
//        pojo.setMrp(mrp);
//
//        ProductData data= dto.convert(pojo);
//        assertEquals(barcode,data.getBarcode());
//        assertEquals(brandCategory,data.getbrandCategory());
//        assertEquals(name,data.getName());
//        assertEquals(mrp,data.getMrp(),0.01);
//    }

//    @Test
//    public void testNormalize(){
//        ProductPojo pojo=new ProductPojo();
//        pojo.setBarcode("BARCODE");
//        pojo.setName("NAME");
//
//        normalize(pojo);
//        assertEquals("barcode",pojo.getBarcode());
//        assertEquals("name",pojo.getName());
//
//    }
}

