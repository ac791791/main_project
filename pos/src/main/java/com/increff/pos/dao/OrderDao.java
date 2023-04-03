package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static com.increff.pos.util.Constants.pageSize;

@Repository
public class OrderDao extends AbstractDao {
    private static String SELECT_ID="select p from OrderPojo p where id=:id";
    private static String SELECT_ALL="select p from OrderPojo p order by id desc";
    private static String DELETE_ID="delete from OrderPojo where id=:id";
    private static String TOTAL_ROWS="Select count(*) from OrderPojo p";

    @Transactional
    public void insert(OrderPojo p){
        em.persist(p);
    }

    public void delete(int id){
        Query query= em.createQuery(DELETE_ID);
        query.setParameter("id",id);
        query.executeUpdate();
    }

    public OrderPojo select(int id){
        TypedQuery<OrderPojo> query=em.createQuery(SELECT_ID,OrderPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }


    public List<OrderPojo> selectAll(){
        TypedQuery<OrderPojo> query=em.createQuery(SELECT_ALL,OrderPojo.class);
        return query.getResultList();
    }

    public List<OrderPojo> selectLimited(int pageNo){
        TypedQuery<OrderPojo> query=em.createQuery(SELECT_ALL,OrderPojo.class);
        query.setFirstResult(pageSize*(Math.max(pageNo-1,0)));
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public int totalRows(){
        TypedQuery<Long> query=em.createQuery(TOTAL_ROWS, Long.class);
        int rows=Integer.parseInt(String.valueOf(getSingle(query)));
        return rows;

    }

}
