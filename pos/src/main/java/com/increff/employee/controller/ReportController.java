package com.increff.employee.controller;

import com.increff.employee.model.*;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.service.DailyReportService;
import com.increff.employee.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api
@RestController
public class ReportController {

    @Autowired
    private ReportService service;

    @Autowired
    private DailyReportService dailyReportService;
    @ApiOperation(value = "Getting Sales Report")
    @RequestMapping(path = "/api/report/salesReport", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm form){

        return service.getSalesReport(form);
    }

    @ApiOperation(value = "Getting Brand Report")
    @RequestMapping(path = "api/report/brandReport", method = RequestMethod.POST)
    public List<BrandReportData> getBrandReport(@RequestBody BrandReportForm form){
        return service.getBrandReport(form);
    }

    @ApiOperation(value = "Getting inventory Report")
    @RequestMapping(path = "api/report/inventoryReport", method = RequestMethod.POST)
    public List<InventoryReportData> getInventoryReport(@RequestBody InventoryReportForm form){
        return service.getInventoryReport(form);
    }

    @ApiOperation(value = "Getting Daily Reports")
    @RequestMapping(path = "api/report/dailyReport", method = RequestMethod.GET)
    public List<DailyReportData> getDailyReport(){
        return dailyReportService.getAll();
    }
}
