package com.increff.employee.dao;

import com.increff.employee.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{

    private static String delete_orderId ="delete from OrderItemPojo p where orderId=:orderId";
    private static String delete_id ="delete from OrderItemPojo p where id=:id";
    private static String select_all="select p from OrderItemPojo p";
    private static String select_orderId="select p from OrderItemPojo p where orderId=:orderId";
    private static String select_id="select p from OrderItemPojo p where id=:id";
    private static String select_orderId_ProductId="select p from OrderItemPojo p where orderId=:orderId AND productId=:productId";


    @Transactional
    public void insert(OrderItemPojo p){
        em.persist(p);
    }
    public void delete(int orderId){
       Query query= em.createQuery(delete_orderId);
       query.setParameter("orderId",orderId);
       query.executeUpdate();
    }

    public void delete_id(int id){
        Query query= em.createQuery(delete_id);
        query.setParameter("id",id);
        query.executeUpdate();
    }

    public OrderItemPojo select_id(int id){
        TypedQuery<OrderItemPojo> query=em.createQuery(select_id,OrderItemPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }

    public OrderItemPojo select_orderIdAndProductId(int orderId, int productId){
        TypedQuery<OrderItemPojo> query=em.createQuery(select_orderId_ProductId,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        query.setParameter("productId",productId);
        return getSingle(query);
    }
    public List<OrderItemPojo> select(int orderId){
        TypedQuery<OrderItemPojo> query= em.createQuery(select_orderId,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }
    public List<OrderItemPojo> selectAll(){
        TypedQuery<OrderItemPojo> query=em.createQuery(select_all,OrderItemPojo.class);
        return query.getResultList();
    }


    public void update(OrderItemPojo p){

    }

}
