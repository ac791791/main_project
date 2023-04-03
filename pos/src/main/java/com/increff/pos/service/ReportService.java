package com.increff.pos.service;

import com.increff.pos.model.*;
import com.increff.pos.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.StringUtil.isEmpty;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ReportService {

    @Autowired
    private ReportDao dao;



    public List<Tuple> getBrandReport(BrandReportForm form){
        String brand = form.getBrand();
        String category = form.getCategory();
        return dao.getBrandReport(brand,category);
    }

    public List<Tuple> getInventoryReport(InventoryReportForm form){
        String brand = form.getBrand();
        String category = form.getCategory();
        return dao.getInventoryReport(brand,category);
    }

    public List<Tuple> getSalesReport(SalesReportForm form) throws ApiException {
        LocalDateTime end;
        LocalDateTime start;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(isEmpty(form.getStartDate())){
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(364);
        }
        else {
            LocalDate startDate = LocalDate.parse(form.getStartDate(), formatter);
            start = startDate.atTime(LocalTime.MIN);
        }
        if(isEmpty(form.getEndDate())) {
            end=java.time.LocalDateTime.now();

        }
        else{
            LocalDate endDate = LocalDate.parse(form.getEndDate(), formatter);
            end = endDate.atTime(LocalTime.MAX);
        }

        if(start.compareTo(end)>0){
            throw new ApiException("Start date can't exceed End Date");
        }
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        if (daysBetween > 365) {
            throw new ApiException("Maximum gap allowed is 356 days");
        }

        String brand = form.getBrand();
        String category = form.getCategory();

        return dao.getSalesReport(start, end, brand, category);
    }


}
