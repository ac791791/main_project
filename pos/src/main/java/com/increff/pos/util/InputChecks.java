package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;


import static com.increff.pos.util.Constants.*;
import static com.increff.pos.util.StringUtil.isEmpty;

public class InputChecks {

    //For Brand Pojo
    public static void validateBrandForm(BrandForm form) throws ApiException {
        if(isEmpty(form.getBrand())){
            throw new ApiException("Brand can't be null/empty. ");
        }
        if(isEmpty(form.getCategory())){
            throw new ApiException("Category can't be null/empty. ");
        }
        if(form.getBrand().length()>maxStringLength){
            throw new ApiException("Length of brand can't exceed "+maxStringLength);
        }
        if(form.getCategory().length()>maxStringLength){
            throw new ApiException("Length of category can't exceed "+maxStringLength);
        }

    }


    //For ProductPojo
    public static void validateProductForm(ProductForm form) throws ApiException {

        if (isEmpty(form.getName()))
            throw new ApiException("Name can't be null/empty");

        if (isEmpty(form.getBarcode()))
            throw new ApiException("Barcode can't be null/empty");

        if (form.getMrp()<=0)
            throw new ApiException("Mrp can't be lesser than or equal to 0");

        if (form.getMrp()>maxMrp)
            throw new ApiException("Mrp can't be greater than "+maxMrp);

        if(form.getName().length()>maxStringLength)
            throw new ApiException("Length of name can't exceed "+maxStringLength);

        if(form.getBarcode().length()>maxStringLength)
            throw new ApiException("Length of barcode can't exceed "+maxStringLength);

        if(form.getbrandCategory()==0)
            throw new ApiException("Please choose Brand/Category Option");

    }


    public static void validateProductUpdateForm(ProductUpdateForm form) throws ApiException {

        if (isEmpty(form.getName())){
            throw new ApiException("Name can't be null/empty");
        }
        if (form.getMrp()<=0){
            throw new ApiException("Mrp can't be lesser than or equal to 0");
        }
        if (form.getMrp()>maxMrp) {
            throw new ApiException("Mrp can't be greater than "+maxMrp);
        }
        if(form.getName().length()>maxStringLength){
            throw new ApiException("Length of name can't exceed "+maxStringLength);
        }

    }


    //For InventoryPojo

    public static void validateInventoryAddForm(InventoryAddForm form) throws ApiException{
        if(form.getQuantity()<0){
            throw new ApiException("Quantity can't be less than 0");
        }
        if(Math.ceil(form.getQuantity())!=form.getQuantity()){
            throw new ApiException("Quantity can't be in Decimals");
        }
        if(form.getQuantity()>maxQuantity){
            throw new ApiException("Quantity can't be greater than "+maxQuantity);
        }
        if(isEmpty(form.getBarcode())){
            throw new ApiException("Barcode can't be null/empty");
        }
    }

    public static void validateInventoryUpdateForm(InventoryUpdateForm form) throws ApiException{
        if(form.getQuantity()<0){
            throw new ApiException("Quantity can't be less than 0");
        }
        if(Math.ceil(form.getQuantity())!=form.getQuantity()){
            throw new ApiException("Quantity can't be in Decimals");
        }
        if(form.getQuantity()>maxQuantity){
            throw new ApiException("Quantity can't be greater than "+maxQuantity);
        }
    }
    public static void validateUserForm(UserForm form) throws ApiException {
        if (isEmpty(form.getEmail())){
            throw new ApiException("Email can't be null/empty");
        }
        if (isEmpty(form.getPassword())){
            throw new ApiException("Password can't be null/empty");
        }

        if(form.getEmail().length()>maxStringLength){
            throw new ApiException("Length of email can't exceed "+maxStringLength);
        }
        if(form.getPassword().length()>maxStringLength){
            throw new ApiException("Length of password can't exceed "+maxStringLength);
        }
    }

    public static void validateChangePasswordForm(ChangePasswordForm form) throws ApiException {
        if (isEmpty(form.getEmail())){
            throw new ApiException("Email can't be null/empty");
        }
        if (isEmpty(form.getNewPassword())){
            throw new ApiException("New Password can't be null/empty");
        }
        if (isEmpty(form.getCurrentPassword())){
            throw new ApiException("Current Password can't be null/empty");
        }
        if (isEmpty(form.getConfirmPassword())){
            throw new ApiException("Confirm Password can't be null/empty");
        }

        if(form.getEmail().length()>30){
            throw new ApiException("Length of email can't exceed "+40);
        }
        if(form.getNewPassword().length()>30){
            throw new ApiException("Length of password can't exceed "+40);
        }
    }



    public static void validateOrderForm(OrderForm form, ProductPojo productPojo, InventoryPojo inventoryPojo) throws ApiException {
        if(form.getQuantity()<1)
            throw new ApiException("Quantity can't be less than 1");
        if(form.getSellingPrice() > productPojo.getMrp() )
            throw new ApiException("Selling Price is greater than Mrp: " + productPojo.getMrp());
        if(form.getQuantity() > inventoryPojo.getQuantity() )
            throw new ApiException("This much quantity is not present. Max Quantity: "+inventoryPojo.getQuantity());
    }

    public static void validateOrderForm(OrderForm form) throws ApiException {
        if (isEmpty(form.getBarcode()))
            throw new ApiException("Barcode can't be null/empty");

        if(form.getQuantity()>maxQuantity)
            throw new ApiException("Quantity can't be greater than "+maxQuantity);

        if(form.getQuantity()<1)
            throw new ApiException("Quantity can't be lesser than 1");

        if (form.getSellingPrice()>maxMrp)
            throw new ApiException("Selling Price can't be greater than "+maxMrp);

        if (form.getSellingPrice()<0)
            throw new ApiException("Selling Price can't be lesser than 0");

    }
    public static void validateOrderItemForm(OrderItemForm form) throws ApiException {
        if (isEmpty(form.getBarcode()))
            throw new ApiException("Barcode can't be null/empty");

        if(form.getQuantity()>maxQuantity)
            throw new ApiException("Quantity can't be greater than "+maxQuantity);

        if(form.getQuantity()<1)
            throw new ApiException("Quantity can't be lesser than 1");

        if (form.getSellingPrice()>maxMrp)
            throw new ApiException("Selling Price can't be greater than "+maxMrp);

        if (form.getSellingPrice()<0)
            throw new ApiException("Selling Price can't be lesser than 0");

    }

    public static void checkOrderItemParameters(OrderItemPojo pojo, OrderPojo existingOrderPojo,ProductPojo existingProductPojo,InventoryPojo inventoryPojo) throws ApiException {
        if(existingOrderPojo.getInvoiceStatus()==1)
            throw new ApiException("Can't Change: Invoice is generated");

        if(inventoryPojo.getQuantity()<pojo.getQuantity()) {
            throw new ApiException("This much quantity is not present. Max Quantity: " + inventoryPojo.getQuantity());
        }

        if (pojo.getSellingPrice()>existingProductPojo.getMrp())
            throw new ApiException("Selling Price is greater than Mrp: " + existingProductPojo.getMrp());


    }
}
