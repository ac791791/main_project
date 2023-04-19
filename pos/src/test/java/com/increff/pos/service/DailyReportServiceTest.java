package com.increff.pos.service;

import com.increff.pos.dto.AbstractUnitTest;
import com.increff.pos.model.DailyReportData;
import com.increff.pos.pojo.DailyReportPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DailyReportServiceTest extends AbstractUnitTest {

    @Autowired
    private DailyReportService service;


    @Before
    public void setUp(){
        String date ="01/01/2023";
        service.add(date);
    }

    @Test
    public void testAdd(){
        String date ="02/01/2023";
        service.add(date);
    }

    @Test
    public void testGetAll(){
        List<DailyReportData> list1 = service.getAll();

        String date ="02/01/2023";
        service.add(date);

        List<DailyReportData> list2 = service.getAll();
        assertEquals(1,list1.size());
        assertEquals(2,list2.size());

    }

    @Test
    public void testGet(){
        DailyReportPojo pojo = service.get("01/01/2023");

        assertEquals(0,pojo.getTotalInvoice());
        assertEquals(0,pojo.getTotalItems());
        assertEquals(0,pojo.getTotalRevenue(),0.01);
    }

    @Test
    public void testUpdate(){
        String date = "01/01/2023";

        DailyReportPojo pojo = new DailyReportPojo();
        pojo.setTotalInvoice(10);
        pojo.setTotalItems(100);
        pojo.setTotalRevenue(500.50);

        service.update(date,pojo);

        DailyReportPojo getPojo = service.get(date);

        assertEquals(10,getPojo.getTotalInvoice());
        assertEquals(100,getPojo.getTotalItems());
        assertEquals(500.50,getPojo.getTotalRevenue(),0.01);
    }
}
