package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static com.increff.pos.util.ConvertFunction.*;
import static com.increff.pos.util.InputChecks.validateInventoryForm;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InventoryDto {

    @Autowired
    private InventoryService service;
    @Autowired
    private ProductService productService;


    public InventoryData get(int id) {
        ProductPojo productPojo = productService.get(id);
        InventoryPojo inventoryPojo = service.get(id);
        InventoryData data = convertInventoryData(inventoryPojo,productPojo);
        return data;

    }

    public List<InventoryData> getAll(){
        List<InventoryData> inventoryDataList = new ArrayList<InventoryData>();
        List<InventoryPojo> inventoryPojoList = service.getAll();


        for(InventoryPojo pojo: inventoryPojoList) {
            ProductPojo productPojo = productService.get(pojo.getId());
            InventoryData data = convertInventoryData(pojo,productPojo);
            inventoryDataList.add(data);
        }
        return inventoryDataList;

    }

    public List<InventoryData> getLimited(int pageNo) throws ApiException {
        if(pageNo<1)
            throw new ApiException("Page No can't be less than 1");
        List<InventoryData> inventoryDataList = new ArrayList<InventoryData>();
        List<InventoryPojo> inventoryPojoList = service.getLimited(pageNo);
        for(InventoryPojo pojo : inventoryPojoList) {
            ProductPojo productPojo = productService.get(pojo.getId());
            InventoryData data = convertInventoryData(pojo,productPojo);
            inventoryDataList.add(data);
        }
        return inventoryDataList;

    }
    public int totalInventory(){
        return service.totalInventory();
    }

    public void update(int id,InventoryForm form) throws ApiException {
        validateInventoryForm(form);
        InventoryPojo pojo = convertInventoryPojo(form);
        service.update(id,pojo);
    }

    public void topUpdate(InventoryForm form) throws ApiException {
        validateInventoryForm(form);
        InventoryPojo pojo = convertInventoryPojo(form);
        ProductPojo existingProductPojo = productService.getCheck(form.getBarcode());
        pojo.setId(existingProductPojo.getId());
        service.topUpdate(pojo);
    }


}


