package com.increff.pos.util;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;

import static com.increff.pos.util.Constants.maxMrp;
import static com.increff.pos.util.Constants.maxQuantity;
import static com.increff.pos.util.StringUtil.isEmpty;

public class InputChecks {

    //For Brand Pojo
    public static void checkInputs(BrandPojo p) throws ApiException {
        if(isEmpty(p.getBrand())){
            throw new ApiException("Brand can't be empty. ");
        }
        if(isEmpty(p.getCategory())){
            throw new ApiException("Category can't be empty. ");
        }

    }


    //For ProductPojo
    public static void checkInputs(ProductPojo p) throws ApiException {

        if (isEmpty(p.getName())){
            throw new ApiException("Name can't be empty");
        }
        if (p.getMrp()<=0){
            throw new ApiException("Mrp can't be lesser than or equal to 0");
        }
        if (p.getMrp()>maxMrp) {
            throw new ApiException("Mrp can't be greater than 999999");
        }
    }

    //For InventoryPojo

    public static void checkInputs(InventoryPojo p) throws ApiException{
        if(p.getQuantity()<0){
            throw new ApiException("Quantity can't be less than 0");
        }
        if(Math.ceil(p.getQuantity())!=p.getQuantity()){
            throw new ApiException("Quantity can't be in Decimals");
        }
        if(p.getQuantity()>maxQuantity){
            throw new ApiException("Quantity can't be greater than 999999");
        }
    }
}
