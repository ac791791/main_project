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
import static com.increff.pos.util.InputChecks.checkInputs;
import static com.increff.pos.util.NormalizeFunctions.normalize;
import static com.increff.pos.util.StringUtil.isEmpty;

@Repository
public class BrandDto {

    @Autowired
    private BrandService service;


    public void add(BrandForm form) throws ApiException {

        BrandPojo p=convert(form);
        checkInputs(p);
        service.add(normalize(p));
    }

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<BrandForm> forms) throws ApiException{
        for(BrandForm form: forms){
            add(form);
        }
    }

    public BrandData get(int id) {
        BrandPojo p= service.get(id);
        return convert(p);
    }

    //Used in products and reports to display brand category in select
    public List<BrandData> getAll(){
        List<BrandData> list1= new ArrayList<BrandData>();
        List<BrandPojo> list2=service.getAll();

        for(BrandPojo p:list2) {
            list1.add(convert(p));
        }
        return list1;
    }

    public List<BrandData> getLimited(int page){
        List<BrandData> list1= new ArrayList<BrandData>();
        List<BrandPojo> list2=service.getLimited(page);

        for(BrandPojo p:list2) {
            list1.add(convert(p));
        }
        return list1;
    }

    public int totalBrands(){
        return service.totalBrands();
    }


    public void update(int id,BrandForm form) throws ApiException {

        BrandPojo p= convert(form);
        checkInputs(p);
        service.update(id,normalize(p));
    }



}