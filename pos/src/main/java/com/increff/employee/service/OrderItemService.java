package com.increff.employee.service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;
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

            if (p.getSellingPrice() > productPojo.getMrp()) {
                throw new ApiException("Selling Price is greater than Mrp: " + productPojo.getMrp());
            } else {

                dao.insert(p);
            }
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
    @Transactional
    public List<OrderItemPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional
    public void update(OrderItemPojo updatedPojo, OrderItemPojo p, ProductPojo productPojo) throws ApiException {

        if(p.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Selling Price is greater than Mrp: "+productPojo.getMrp());
        }

        else {

            updatedPojo.setSellingPrice(p.getSellingPrice());


            updatedPojo.setQuantity(p.getQuantity());
        }
    }





}
