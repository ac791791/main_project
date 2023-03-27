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
        ProductPojo p= productConvert(form);
        checkInputs(p);  // used to check basic input parameters like name is empty or not , mrp >0 or not
        if (isEmpty(p.getBarcode())){
            throw new ApiException("Barcode can't be empty");
        }
        if(p.getbrandCategory()==0){
            throw new ApiException("Please Select Brand/Category Option");
        }

        BrandPojo brandPojo= brandService.get(p.getbrandCategory());
        if(brandPojo==null){
            throw new ApiException("Brand Category is not found for this product");
        }

        service.add(normalize(p));
        inventoryService.add(createInventoryPojo(p));
    }


    public ProductData get(int id) {
        ProductPojo p= service.get(id);
        ProductData data= productConvert(p);
        BrandPojo brandPojo= brandService.get(p.getbrandCategory());
        data.setBrand(brandPojo.getBrand());
        data.setCategory(brandPojo.getCategory());

        return data;
    }

    public List<ProductData> getAll(){
        List<ProductData> list1= new ArrayList<ProductData>();
        List<ProductPojo> list2= service.getAll();

        for(ProductPojo p: list2) {
            ProductData data= productConvert(p);
            BrandPojo brandPojo= brandService.get(p.getbrandCategory());
            data.setBrand(brandPojo.getBrand());
            data.setCategory(brandPojo.getCategory());

            list1.add(data);
        }
        return list1;
    }

    public List<ProductData> getLimited(int page){

        List<ProductData> list1= new ArrayList<ProductData>();
        List<ProductPojo> list2= service.getLimited(page);

        for(ProductPojo p: list2) {
            ProductData data= productConvert(p);
            BrandPojo brandPojo= brandService.get(p.getbrandCategory());
            data.setBrand(brandPojo.getBrand());
            data.setCategory(brandPojo.getCategory());

            list1.add(data);
        }
        return list1;
    }

    public int totalProducts(){
        return service.totalProducts();
    }

//    public List<ProductPojo> getByBrandCategory(int brandCategory ){
//        return service.getByBrandCategory(brandCategory);
//    }

    public void update(int id, ProductForm form) throws ApiException {
        ProductPojo p=productConvert(form);
        checkInputs(p);
        service.update(id, normalize(p));
    }





}

