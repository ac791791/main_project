package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
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
        else
            dao.insert(p);
    }

    public void deleteByOrderId(int orderId) {
        dao.deleteByOrderId(orderId);
    }


    public void delete(int id) throws ApiException {
        OrderItemPojo pojo= getCheck(id);
        dao.delete(id);
    }

    public List<OrderItemPojo> getByOrderId(int orderId){

        return dao.selectByOrderId(orderId);
    }

    public OrderItemPojo getByOrderIdAndProductId(int orderId,int productId){
        return getByOrderIdAndProductId(orderId,productId);
    }

    public OrderItemPojo getCheck(int id) throws ApiException {
        OrderItemPojo pojo = dao.select(id);
        if(Objects.isNull(pojo))
            throw new ApiException("OrderItem with given id "+id+" does not exists");
        return pojo;
    }

    public void update(int id, OrderItemPojo p) throws ApiException {
        OrderItemPojo updatedPojo= getCheck(id);
            updatedPojo.setSellingPrice(p.getSellingPrice());
            updatedPojo.setQuantity(p.getQuantity());
    }

}
