package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{

    private static String DELETE_ORDER_ID ="delete from OrderItemPojo p where orderId=:orderId";
    private static String DELETE_ID ="delete from OrderItemPojo p where id=:id";
    private static String SELECT_ORDER_ID="select p from OrderItemPojo p where orderId=:orderId";
    private static String SELECT_ID="select p from OrderItemPojo p where id=:id";
    private static String SELECT_ORDER_ID_AND_PRODUCT_ID="select p from OrderItemPojo p where orderId=:orderId AND productId=:productId";


    @Transactional
    public void insert(OrderItemPojo p){
        em.persist(p);
    }
    public void deleteByOrderId(int orderId){
       Query query= em.createQuery(DELETE_ORDER_ID);
       query.setParameter("orderId",orderId);
       query.executeUpdate();
    }

    public void delete(int id){
        Query query= em.createQuery(DELETE_ID);
        query.setParameter("id",id);
        query.executeUpdate();
    }

    public OrderItemPojo select(int id){
        TypedQuery<OrderItemPojo> query=em.createQuery(SELECT_ID,OrderItemPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }

    public OrderItemPojo select_orderIdAndProductId(int orderId, int productId){
        TypedQuery<OrderItemPojo> query=em.createQuery(SELECT_ORDER_ID_AND_PRODUCT_ID,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        query.setParameter("productId",productId);
        return getSingle(query);
    }
    public List<OrderItemPojo> selectByOrderId(int orderId){
        TypedQuery<OrderItemPojo> query= em.createQuery(SELECT_ORDER_ID,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }

}
