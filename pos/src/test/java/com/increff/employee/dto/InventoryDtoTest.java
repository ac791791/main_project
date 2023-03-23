//package com.increff.employee.dto;
//
//import com.increff.employee.model.InventoryData;
//import com.increff.employee.model.InventoryForm;
//import com.increff.employee.pojo.InventoryPojo;
//import com.increff.employee.pojo.ProductPojo;
//import com.increff.employee.service.ApiException;
//import com.increff.employee.service.ProductService;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import static org.junit.Assert.assertEquals;
//
//public class InventoryDtoTest extends AbstractUnitTest{
//
//    @Autowired
//    private InventoryDto dto;
//
//    @Autowired
//    private ProductService productService;
//
//    private int brandCategory=1;
//
//    @Before
//    public void setUp() throws ApiException {
//        ProductPojo productPojo=new ProductPojo();
//        productPojo.setBarcode("b1");
//        productPojo.setbrandCategory(1);
//        productPojo.setName("name");
//        productPojo.setMrp(100.05);
//
//        productService.add(productPojo);
//    }
//
//    @Test
//    public void testGetAll() throws ApiException {
//        List<InventoryData> list=dto.getAll();
//        int size= list.size();
//        assertEquals(1,size);
//
//        ProductPojo productPojo=new ProductPojo();
//        productPojo.setBarcode("b2");
//        productPojo.setbrandCategory(1);
//        productPojo.setName("name2");
//        productPojo.setMrp(100.05);
//
//        productService.add(productPojo);
//
//        List<InventoryData> list2=dto.getAll();
//        int size2= list2.size();
//        assertEquals(2,size2);
//    }
//
//    @Test
//    public void testGet(){
//        List<InventoryData> list=dto.getAll();
//        for(InventoryData data:list){
//            InventoryData getData=dto.get(data.getId());
//            assertEquals(0,getData.getQuantity());
//        }
//    }
//
//    @Test
//    public void testUpdate() throws ApiException {
//        List<InventoryData> list=dto.getAll();
//        for(InventoryData data:list){
//            InventoryForm form=new InventoryForm();
//            form.setId(data.getId());
//            form.setQuantity(10);
//            dto.update(data.getId(), form);
//
//            InventoryData getData=dto.get(data.getId());
//            assertEquals(10,getData.getQuantity());
//        }
//
//    }
//
//    @Test
//    public void testTopUpdate() throws ApiException {
//        List<InventoryData> list=dto.getAll();
//        for(InventoryData data:list){
//            InventoryForm form=new InventoryForm();
//            form.setId(data.getId());
//            form.setQuantity(10);
//            form.setBarcode("b1");
//            dto.topUpdate(form);
//
//            InventoryData getData=dto.get(data.getId());
//            assertEquals(10,getData.getQuantity());
//        }
//    }
//
//    @Test
//    public void testTopUpdateWithoutBarcode(){
//        List<InventoryData> list=dto.getAll();
//        try {
//            for(InventoryData data:list){
//                InventoryForm form=new InventoryForm();
//                form.setId(data.getId());
//                form.setQuantity(10);
//                dto.topUpdate(form);
//
//                InventoryData getData=dto.get(data.getId());
//                assertEquals(10,getData.getQuantity());
//            }
//
//        }catch (ApiException e){
//            assertEquals("Couldn't Update: Product with given barcode do not exist",e.getMessage());
//        }
//    }
//
////    @Test
////    public void testFormToPojoConvert(){
////        InventoryForm form=new InventoryForm();
////        form.setId(1);
////        form.setQuantity(10);
////
////        InventoryPojo pojo=dto.convert(form);
////        assertEquals(1,pojo.getId());
////        assertEquals(10,pojo.getQuantity());
////    }
////
////    @Test
////    public void testPojoToDataConvert(){
////        List<InventoryData> list=dto.getAll();
////        for(InventoryData d:list) {
////            InventoryPojo pojo = new InventoryPojo();
////            pojo.setId(d.getId());
////            pojo.setQuantity(d.getQuantity());
////
////            InventoryData data = dto.convert(pojo);
////            assertEquals(pojo.getId(), data.getId());
////            assertEquals(pojo.getQuantity(), data.getQuantity());
////            assertEquals("name", data.getName());
////            assertEquals("b1", data.getBarcode());
////        }
////    }
//}
