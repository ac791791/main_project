package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.model.DailyReportData;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConvertFunction.*;


@Service
@Transactional(rollbackOn = ApiException.class)
public class DailyReportService {

    @Autowired
    private DailyReportDao dao;


    public void add(String date){
        DailyReportPojo p=new DailyReportPojo();
        p.setDate(date);
        p.setTotalInvoice(0);
        p.setTotalItems(0);
        p.setTotalRevenue(0);
        dao.insert(p);

    }

    public DailyReportPojo get(String date){
         return dao.select(date);
    }

    public List<DailyReportData> getAll(){
        List<DailyReportData> dailyReportDataList=new ArrayList<DailyReportData>();
        List<DailyReportPojo> dailyReportPojoList=dao.selectAll();

        for(DailyReportPojo pojo:dailyReportPojoList){
            DailyReportData data=convertDailyReportData(pojo);
            dailyReportDataList.add(data);
        }
        return dailyReportDataList;
    }


    public void update(String date, DailyReportPojo p){
        DailyReportPojo pojo=get(date);
        pojo.setTotalInvoice(pojo.getTotalInvoice()+p.getTotalInvoice());
        pojo.setTotalItems(pojo.getTotalItems()+p.getTotalItems());
        pojo.setTotalRevenue(pojo.getTotalRevenue()+p.getTotalRevenue());
    }
}
