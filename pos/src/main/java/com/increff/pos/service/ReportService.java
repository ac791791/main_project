package com.increff.pos.service;

import com.increff.pos.model.*;
import com.increff.pos.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.StringUtil.isEmpty;

@Service
public class ReportService {

    @Autowired
    private ReportDao dao;

    @Transactional
    public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException {

        return dao.getSalesReport(form);
    }

    @Transactional
    public List<BrandReportData> getBrandReport(BrandReportForm form){
        return dao.getBrandReport(form);
    }

    @Transactional
    public List<InventoryReportData> getInventoryReport(InventoryReportForm form){
        return dao.getInventoryReport(form);
    }




}