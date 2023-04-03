package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DailyReportService;
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
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportDto dto;

    @Autowired
    private DailyReportService dailyReportService;

    @ApiOperation(value = "Getting Brand Report")
    @RequestMapping(value = "/brandReport", method = RequestMethod.POST)
    public List<BrandReportData> getBrandReport(@RequestBody BrandReportForm form){
        return dto.getBrandReport(form);
    }

    @ApiOperation(value = "Getting inventory Report")
    @RequestMapping(value = "/inventoryReport", method = RequestMethod.POST)
    public List<InventoryReportData> getInventoryReport(@RequestBody InventoryReportForm form){
        return dto.getInventoryReport(form);
    }
    @ApiOperation(value = "Getting Sales Report")
    @RequestMapping(value = "/salesReport", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm form) throws ApiException {

        return dto.getSalesReport(form);
    }

    @ApiOperation(value = "Getting Daily Reports")
    @RequestMapping(value = "/dailyReport", method = RequestMethod.GET)
    public List<DailyReportData> getDailyReport(){
        return dailyReportService.getAll();
    }
}
