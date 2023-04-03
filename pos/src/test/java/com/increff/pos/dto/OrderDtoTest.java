//package com.increff.pos.dto;
//
//import com.increff.pos.model.OrderData;
//import com.increff.pos.service.ApiException;
//import com.increff.pos.service.OrderService;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static com.increff.pos.util.Constants.pageSize;
//import static org.junit.Assert.assertEquals;
//
//public class OrderDtoTest extends AbstractUnitTest {
//
//    @Autowired
//    private OrderDto dto;
//
//    @Autowired
//    private OrderService service;
//
//    @Before
//    public void setUp() {
//        dto.addOrder();
//    }
//
//    @Test
//    public void testAdd() {
//        dto.addOrder();
//    }
//
//    @Test
//    public void testGetAll() {
//        List<OrderData> list1 = dto.getAll();
//        assertEquals(1, list1.size());
//        dto.addOrder();
//        List<OrderData> list2 = dto.getAll();
//        assertEquals(2, list2.size());
//    }
//
//    @Test
//    public void testGet() {
//        List<OrderData> list = dto.getAll();
//
//        for (OrderData data : list) {
//            OrderData getData = dto.get(data.getId());
//
//            assertEquals(data.getTime(), getData.getTime());
//            assertEquals(data.getInvoiceStatus(), getData.getInvoiceStatus());
//        }
//    }
//
//
//    @Test
//    public void testGetLimited() {
//        for (int i = 0; i < 15; i++) {
//            dto.addOrder();
//        }
//        assertEquals(pageSize, dto.getLimited(1).size());
//        assertEquals(16 - pageSize, dto.getLimited(2).size());
//    }
//
//    @Test
//    public void testTotalOrders() {
//        int total = dto.totalOrders();
//
//        assertEquals(1, total);
//        dto.addOrder();
//        total = dto.totalOrders();
//        assertEquals(2, total);
//    }
//
//    @Test
//    public void testDelete() throws ApiException {
//        List<OrderData> list = dto.getAll();
//
//        for (OrderData data : list) {
//            dto.delete(data.getId());
//        }
//        List<OrderData> list1 = dto.getAll();
//        assertEquals(0, list1.size());
//    }
//
//    @Test
//    public void testNotZeroInvoiceStatus() throws ApiException {
//        List<OrderData> list = dto.getAll();
//        for (OrderData data : list) {
//            service.changeInvoiceStatus(data.getId());
//        }
//        try {
//            for (OrderData data : list) {
//                dto.delete(data.getId());
//            }
//            List<OrderData> list1 = dto.getAll();
//            assertEquals(0, list1.size());
//        }
//        catch (ApiException e){
//            assertEquals("Can't Delete: Invoice is generated",e.getMessage());
//        }
//
//
//    }
//}
