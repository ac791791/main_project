package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao{

    private static String select_id ="select p from DailyReportPojo p where date=:date";
    private static String select_all="select p from DailyReportPojo p order by date desc";


    @Transactional
    public void insert(DailyReportPojo p) {
        em.persist(p);
    }

    public DailyReportPojo select(String date) {
        TypedQuery<DailyReportPojo> query= em.createQuery(select_id,DailyReportPojo.class);
        query.setParameter("date", date);
        return getSingle(query);
    }

    public List<DailyReportPojo> selectAll(){
        TypedQuery<DailyReportPojo> query=em.createQuery(select_all, DailyReportPojo.class);
        return query.getResultList();
    }
}
