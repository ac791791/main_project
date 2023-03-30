package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.util.ConvertFunction.*;
import static com.increff.pos.util.InputChecks.checkInputs;
import static com.increff.pos.util.NormalizeFunctions.normalize;
import static com.increff.pos.util.StringUtil.isEmpty;

@Repository
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;

    public void add(ProductForm form) throws ApiException {
        ProductPojo pojo= convertProductPojo(form);
        checkInputs(pojo);  // used to check basic input parameters like name is empty or not , mrp >0 or not
        if (isEmpty(pojo.getBarcode())){
            throw new ApiException("Barcode can't be empty");
        }

        BrandPojo existingBrandPojo= brandService.get(pojo.getbrandCategory());
        if(Objects.isNull(existingBrandPojo)){
            throw new ApiException("Brand Category is not found for this product");
        }
        
        service.add(normalize(pojo));
        inventoryService.add(createInventoryPojo(pojo));
    }


    public ProductData get(int id) {
        ProductPojo pojo= service.get(id);
        //TODO make different function for it
        ProductData data= convertProductData(pojo);
        BrandPojo brandPojo= brandService.get(pojo.getbrandCategory());
        data.setBrand(brandPojo.getBrand());
        data.setCategory(brandPojo.getCategory());

        return data;
    }

    public List<ProductData> getAll(){
        List<ProductData> productDataList= new ArrayList<ProductData>();
        List<ProductPojo> productPojoList= service.getAll();

        for(ProductPojo pojo: productPojoList) {
            ProductData data= convertProductData(pojo);
            BrandPojo brandPojo= brandService.get(pojo.getbrandCategory());
            data.setBrand(brandPojo.getBrand());
            data.setCategory(brandPojo.getCategory());

            productDataList.add(data);
        }
        return productDataList;
    }

    public List<ProductData> getLimited(int pageNo){

        List<ProductData> productDataList= new ArrayList<ProductData>();
        List<ProductPojo> productPojoList= service.getLimited(pageNo);

        for(ProductPojo pojo: productPojoList) {
            ProductData data= convertProductData(pojo);
            BrandPojo brandPojo= brandService.get(pojo.getbrandCategory());
            data.setBrand(brandPojo.getBrand());
            data.setCategory(brandPojo.getCategory());

            productDataList.add(data);
        }
        return productDataList;
    }

    public int totalProducts(){
        return service.totalProducts();
    }

//    public List<ProductPojo> getByBrandCategory(int brandCategory ){
//        return service.getByBrandCategory(brandCategory);
//    }

    //TODO to use UpdateProductForm
    public void update(int id, ProductForm form) throws ApiException {
        ProductPojo pojo=convertProductPojo(form);
        checkInputs(pojo);
        service.update(id, normalize(pojo));
    }





}

