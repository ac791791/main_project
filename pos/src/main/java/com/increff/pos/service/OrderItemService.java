package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;



    @Transactional
    public void add(OrderItemPojo p, ProductPojo productPojo) throws ApiException {


        OrderItemPojo pojo= dao.select_orderIdAndProductId(p.getOrderId(),p.getProductId());
        if(pojo!=null) {
            if (pojo.getSellingPrice() != p.getSellingPrice()) {
                throw new ApiException("Already present in cart. Selling Price can't be different.");

            }
            else{
                int totalQuantity= pojo.getQuantity()+p.getQuantity();
                pojo.setQuantity(totalQuantity);
            }
        }
        else {

                dao.insert(p);

        }
    }
    @Transactional
    public void delete(int orderId) {

        dao.delete(orderId);
    }

    @Transactional
    public void delete_id(int id){
        dao.delete_id(id);
    }

    @Transactional
    public List<OrderItemPojo> get(int orderId){
        return dao.select(orderId);
    }

    @Transactional
    public OrderItemPojo get_id(int id){
        return dao.select_id(id);
    }
//    @Transactional
//    public List<OrderItemPojo> getAll(){
//        return dao.selectAll();
//    }

    @Transactional
    public void update(OrderItemPojo updatedPojo, OrderItemPojo p) throws ApiException {


            updatedPojo.setSellingPrice(p.getSellingPrice());

            updatedPojo.setQuantity(p.getQuantity());

    }





}
