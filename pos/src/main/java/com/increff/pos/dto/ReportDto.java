package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DailyReportService;
import com.increff.pos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

import static com.increff.pos.util.ConvertFunction.*;

@Repository
public class ReportDto {

    @Autowired
    private ReportService service;

    @Autowired
    private DailyReportService dailyReportService;

    public List<BrandReportData> getBrandReport(BrandReportForm form){
        List<Tuple> tupleList = service.getBrandReport(form);
        return convertBrandReportData(tupleList);
    }

    public List<InventoryReportData> getInventoryReport(InventoryReportForm form){
        List<Tuple> tupleList = service.getInventoryReport(form);
        return convertInventoryReportData(tupleList);
    }

    public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException {
        List<Tuple> tupleList=service.getSalesReport(form);
        return convertSalesReportData(tupleList);
    }
}
