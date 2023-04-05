package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderDao dao;

    public void addOrder(OrderPojo p){
        dao.insert(p);
    }


    public void delete(int orderId) throws ApiException {
        dao.delete(orderId);
    }

    public OrderPojo getCheck(int id) throws ApiException {
        OrderPojo pojo = dao.select(id);
        if(Objects.isNull(pojo))
            throw new ApiException("Order with given id "+id+" does not exist");
        return pojo;
    }

    public List<OrderPojo> getAll(){
        return dao.selectAll();
    }

    public List<OrderPojo> getLimited(int pageNo){
        return dao.selectLimited(pageNo);
    }


    public int totalOrders(){
        return dao.totalRows();
    }


    public void changeInvoiceStatus(int orderId){
        OrderPojo orderPojo=dao.select(orderId);
        int value=1;
        orderPojo.setInvoiceStatus(value);
    }

}
