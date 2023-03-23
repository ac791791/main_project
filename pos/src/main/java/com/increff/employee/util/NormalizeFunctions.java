package com.increff.employee.util;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;

public class NormalizeFunctions {

    public static BrandPojo normalize(BrandPojo p){
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
        return p;
    }

    public static ProductPojo normalize(ProductPojo p){
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setName(StringUtil.toLowerCase(p.getName()));
        return p;
    }
}
