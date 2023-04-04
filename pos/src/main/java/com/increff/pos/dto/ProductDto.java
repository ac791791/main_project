package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductUpdateForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


import static com.increff.pos.util.ConvertFunction.*;
import static com.increff.pos.util.InputChecks.*;
import static com.increff.pos.util.NormalizeFunctions.normalize;

@Repository
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;

    public void add(ProductForm form) throws ApiException {
        validateProductForm(form);
        ProductPojo pojo = convertProductPojo(form);
        BrandPojo existingBrandPojo = brandService.get(pojo.getbrandCategory());
        checkAddProductsParameters(pojo,existingBrandPojo);
        
        service.add(normalize(pojo));
        inventoryService.add(createInventoryPojo(pojo));
    }


    public ProductData get(int id) {
        ProductPojo pojo = service.get(id);
        BrandPojo brandPojo = brandService.get(pojo.getbrandCategory());
        ProductData data = convertProductData(pojo, brandPojo);
        return data;
    }

    public List<ProductData> getAll(){
        List<ProductData> productDataList = new ArrayList<ProductData>();
        List<ProductPojo> productPojoList = service.getAll();

        for(ProductPojo pojo: productPojoList) {
            BrandPojo brandPojo = brandService.get(pojo.getbrandCategory());
            ProductData data = convertProductData(pojo,brandPojo);
            productDataList.add(data);
        }
        return productDataList;
    }

    public List<ProductData> getLimited(int pageNo) throws ApiException {
        if(pageNo<1)
            throw new ApiException("Page No can't be less than 1");

        List<ProductData> productDataList = new ArrayList<ProductData>();
        List<ProductPojo> productPojoList = service.getLimited(pageNo);

        for(ProductPojo pojo: productPojoList) {
            BrandPojo brandPojo = brandService.get(pojo.getbrandCategory());
            ProductData data = convertProductData(pojo,brandPojo);
            productDataList.add(data);
        }
        return productDataList;
    }

    public int totalProducts(){
        return service.totalProducts();
    }


    public void update(int id, ProductUpdateForm form) throws ApiException {
        validateProductUpdateForm(form);
        String name=StringUtil.toLowerCase(form.getName());
        service.update(id,name,form.getMrp());
    }




}

