package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao dao;



    @Transactional
    public void addOrder(OrderPojo p){
        dao.insert(p);
    }

    @Transactional
    public void delete(int orderId){
        dao.delete(orderId);

    }
    @Transactional
    public OrderPojo get(int id){
        return dao.select(id);
    }

    @Transactional
    public List<OrderPojo> getAll(){
        return dao.selectAll();
    }
    @Transactional
    public List<OrderPojo> getLimited(int page){
        return dao.selectLimited(page);
    }

    @Transactional
    public int totalOrders(){
        return dao.totalRows();
    }
    @Transactional
    public OrderPojo getRecentOrder(){
        return dao.selectRecentOrder();
    }
    @Transactional
    public void changeInvoiceStatus(int orderId){
        OrderPojo orderPojo=dao.select(orderId);
        int value=1;
        orderPojo.setInvoiceStatus(value);

    }


}
