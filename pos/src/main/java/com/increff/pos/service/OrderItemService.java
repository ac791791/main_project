package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;


@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;



    public void add(OrderItemPojo p) throws ApiException {
        OrderItemPojo existingOrderItemPojo = dao.select_orderIdAndProductId(p.getOrderId(),p.getProductId());

        if(Objects.nonNull(existingOrderItemPojo)) {
            if (existingOrderItemPojo.getSellingPrice() != p.getSellingPrice())
                throw new ApiException("Already present in cart. Selling Price can't be different.");

            int totalQuantity = existingOrderItemPojo.getQuantity()+p.getQuantity();
            existingOrderItemPojo.setQuantity(totalQuantity);
        }
        else {
            System.out.println("check3");
            dao.insert(p);
        }
    }

    public void deleteByOrderId(int orderId) {
        dao.deleteByOrderId(orderId);
    }


    public void delete(int id){
        dao.delete(id);
    }

    public List<OrderItemPojo> getByOrderId(int orderId){
        return dao.selectByOrderId(orderId);
    }

    public OrderItemPojo getByOrderIdAndProductId(int orderId,int productId){
        return getByOrderIdAndProductId(orderId,productId);
    }


    public OrderItemPojo get(int id){
        return dao.select(id);
    }

//    @Transactional
//    public List<OrderItemPojo> getAll(){
//        return dao.selectAll();
//    }


    public void update(OrderItemPojo updatedPojo, OrderItemPojo p) throws ApiException {
            updatedPojo.setSellingPrice(p.getSellingPrice());
            updatedPojo.setQuantity(p.getQuantity());
    }





}
