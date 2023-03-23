package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.increff.employee.util.Constants.pageRows;

@Repository
public class OrderDao extends AbstractDao {
    private static String select_id="select p from OrderPojo p where id=:id";
    private static String select_all="select p from OrderPojo p order by id desc";
    private static String delete_id="delete from OrderPojo where id=:id";

    private static String select_maxId="select p from OrderPojo p where id=(select max(id) from OrderPojo)";
    private static String total_rows="Select count(*) from OrderPojo p";

    @Transactional
    public void insert(OrderPojo p){
        em.persist(p);
    }

    public void delete(int id){
        Query query= em.createQuery(delete_id);
        query.setParameter("id",id);
        query.executeUpdate();
    }
    public OrderPojo select(int id){
        TypedQuery<OrderPojo> query=em.createQuery(select_id,OrderPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }

    public OrderPojo selectRecentOrder(){
        TypedQuery<OrderPojo> query=em.createQuery(select_maxId,OrderPojo.class);
        return getSingle(query);
    }

//    public List<OrderPojo> selectAll(){
//        TypedQuery<OrderPojo> query=em.createQuery(select_all,OrderPojo.class);
//        return query.getResultList();
//    }

    public List<OrderPojo> selectLimited(int page){
        TypedQuery<OrderPojo> query=em.createQuery(select_all,OrderPojo.class);
        query.setFirstResult(pageRows*(page-1));
        query.setMaxResults(pageRows);
        return query.getResultList();
    }

    public int totalRows(){
        TypedQuery<Long> query=em.createQuery(total_rows, Long.class);
        int rows=Integer.parseInt(String.valueOf(getSingle(query)));
        return rows;

    }


}
