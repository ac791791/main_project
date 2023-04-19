//package com.increff.pos.util;
//
//import com.increff.pos.dto.AbstractUnitTest;
//import com.increff.pos.model.BrandForm;
//import com.increff.pos.service.ApiException;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static com.increff.pos.util.Constants.maxStringLength;
//import static org.junit.Assert.assertEquals;
//
//public class InputChecksTest extends AbstractUnitTest {
//
//    @Autowired
//    private InputChecks checks;
//    @Test
//    public void testValidateBrandFormGreaterBrandLength() throws ApiException {
//        BrandForm brandForm = new BrandForm();
//        brandForm.setBrand("abcdefghijkabcdefghijkabcdefghijkabcdefghijkabcdefghijk");
//        brandForm.setCategory("categoty");
//
//        try {
//            checks.validateBrandForm(brandForm);
//        }
//        catch (ApiException e){
//            assertEquals("Length of brand can't exceed "+maxStringLength,e.getMessage());
//        }
//    }
//
//}
