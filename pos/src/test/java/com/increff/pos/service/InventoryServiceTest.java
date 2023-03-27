package com.increff.pos.service;

import com.increff.pos.dto.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryServiceTest extends AbstractUnitTest {

    @Autowired
    private InventoryService service;

    @Before
    public void setUp(){
        InventoryPojo p=new InventoryPojo();
        p.setId(1);
        p.setQuantity(10);
        service.add(p);
    }

    @Test
    public void testAdd(){
        InventoryPojo p=new InventoryPojo();
        p.setId(2);
        p.setQuantity(10);
        service.add(p);
    }



}
