package com.increff.employee.service;

import com.increff.employee.dao.DailyReportDao;
import com.increff.employee.model.DailyReportData;
import com.increff.employee.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.employee.util.ConvertFunction.dailyReportConvert;

@Service
public class DailyReportService {

    @Autowired
    private DailyReportDao dao;

    @Transactional
    public void add(String date){
        System.out.println("Debug1X");
        DailyReportPojo p=new DailyReportPojo();
        p.setDate(date);
        p.setTotalInvoice(0);
        p.setTotalItems(0);
        p.setTotalRevenue(0);
        System.out.println("Debug2X");
        dao.insert(p);
        System.out.println("Debug3X");

    }

    @Transactional
    public DailyReportPojo get(String date){
         return dao.select(date);
    }

    @Transactional
    public List<DailyReportData> getAll(){
        List<DailyReportData> list1=new ArrayList<DailyReportData>();
        List<DailyReportPojo> list2=dao.selectAll();

        for(DailyReportPojo pojo:list2){
            DailyReportData data=dailyReportConvert(pojo);
            list1.add(data);
        }
        return list1;
    }

    @Transactional
    public void update(String date, DailyReportPojo p){
        DailyReportPojo pojo=get(date);
        pojo.setTotalInvoice(pojo.getTotalInvoice()+p.getTotalInvoice());
        pojo.setTotalItems(pojo.getTotalItems()+p.getTotalItems());
        pojo.setTotalRevenue(pojo.getTotalRevenue()+p.getTotalRevenue());
    }
}
