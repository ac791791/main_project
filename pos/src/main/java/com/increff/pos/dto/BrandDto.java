package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConvertFunction.*;
import static com.increff.pos.util.InputChecks.validateBrandForm;
import static com.increff.pos.util.NormalizeFunctions.normalize;

@Repository
public class BrandDto {

    @Autowired
    private BrandService service;


    public void add(BrandForm form) throws ApiException {
        validateBrandForm(form);
        BrandPojo p = convertBrandPojo(form);
        service.add(normalize(p));
    }


    public BrandData getCheck(int id) throws ApiException {
        BrandPojo p = service.getCheck(id);
        return convertBrandData(p);
    }

    //Used in products and reports to display brand category in select
    public List<BrandData> getAll(){
        List<BrandData> brandDataList = new ArrayList<BrandData>();
        List<BrandPojo> brandPojoList = service.getAll();

        for(BrandPojo p:brandPojoList) {
            brandDataList.add(convertBrandData(p));
        }
        return brandDataList;
    }

    public List<BrandData> getLimited(int pageNo) throws ApiException {
        if(pageNo<1)
            throw new ApiException("Page No can't be less than 1");

        List<BrandData> brandDataList = new ArrayList<BrandData>();
        List<BrandPojo> brandPojoList = service.getLimited(pageNo);

        for(BrandPojo p:brandPojoList) {
            brandDataList.add(convertBrandData(p));
        }
        return brandDataList;
    }

    public int totalBrands(){
        return service.totalBrands();
    }


    public void update(int id,BrandForm form) throws ApiException {
        validateBrandForm(form);
        BrandPojo p = convertBrandPojo(form);
        service.update(id,normalize(p));
    }

}
