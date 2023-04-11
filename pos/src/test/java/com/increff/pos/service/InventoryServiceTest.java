package com.increff.pos.service;

import com.increff.pos.dto.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testUpdate() throws ApiException {
        int quantity=50;
        service.update(1,quantity);

        InventoryPojo pojo = service.get(1);
        assertEquals(quantity,pojo.getQuantity());
    }

    @Test
    public void testUpdateNullProduct() throws ApiException {
        int quantity=50;
        try {
            service.update(2,quantity);
        }
        catch (ApiException e){
            assertEquals("Product with given id 2 does not exist",e.getMessage());
        }
        InventoryPojo pojo = service.get(1);
        assertEquals(10,pojo.getQuantity());
    }



}
