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
import static com.increff.pos.util.InputChecks.checkInputs;
import static com.increff.pos.util.StringUtil.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InventoryDto {

    @Autowired
    private InventoryService service;
    @Autowired
    private ProductService productService;


    public InventoryData get(int id) {
        InventoryData data=convertInventoryData(service.get(id));
        ProductPojo productPojo= productService.get(data.getId());
        data.setName(productPojo.getName());
        data.setBarcode(productPojo.getBarcode());
        return data;

    }

    public List<InventoryData> getAll(){
        List<InventoryData> inventoryDataList= new ArrayList<InventoryData>();
        List<InventoryPojo> inventoryPojoList = service.getAll();

        for(InventoryPojo pojo: inventoryPojoList) {
            InventoryData data=convertInventoryData(pojo);
            ProductPojo productPojo= productService.get(data.getId());
            data.setName(productPojo.getName());
            data.setBarcode(productPojo.getBarcode());
            inventoryDataList.add(data);
        }
        return inventoryDataList;

    }

    public List<InventoryData> getLimited(int pageNo){
        List<InventoryData> inventoryDataList= new ArrayList<InventoryData>();
        List<InventoryPojo> inventoryPojoList = service.getLimited(pageNo);
        for(InventoryPojo pojo: inventoryPojoList) {
            InventoryData data=convertInventoryData(pojo);
            ProductPojo productPojo= productService.get(data.getId());
            data.setName(productPojo.getName());
            data.setBarcode(productPojo.getBarcode());
            inventoryDataList.add(data);
        }
        return inventoryDataList;

    }
    public int totalInventory(){
        return service.totalInventory();
    }

    public void update(int id,InventoryForm form) throws ApiException {
        InventoryPojo pojo=convertInventoryPojo(form);
        checkInputs(pojo);
        service.update(id,pojo);
    }

    public void topUpdate(InventoryForm form) throws ApiException {
        InventoryPojo pojo = convertInventoryPojo(form);
        checkInputs(pojo);
        if(isEmpty(form.getBarcode())){
            throw new ApiException("Barcode can't be empty");
        }

        ProductPojo existingProductPojo= productService.getCheck(form.getBarcode());

        pojo.setId(existingProductPojo.getId());
        service.topUpdate(pojo);
    }


}


