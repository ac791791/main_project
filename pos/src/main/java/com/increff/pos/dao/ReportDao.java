package com.increff.pos.dao;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import static com.increff.pos.util.StringUtil.isEmpty;

@Repository
public class ReportDao extends AbstractDao{

    private static String BRAND_REPORT=" SELECT brand AS brand, category AS category from BrandPojo "+
            "WHERE (:brand = '' OR brand = :brand) " +
            "AND (:category = '' OR category = :category)";

    private static String INVENTORY_REPORT="Select brandPojo.brand AS brand, brandPojo.category AS category,"+
            "productPojo.barcode AS barcode, productPojo.name AS name,inventoryPojo.quantity AS quantity "+
            "FROM BrandPojo brandPojo "+
            "INNER JOIN ProductPojo productPojo ON brandPojo.id=productPojo.brandCategory "+
            "INNER JOIN InventoryPojo inventoryPojo ON productPojo.id=inventoryPojo.id "+
            "WHERE (:brand = '' OR brand = :brand) " +
            "AND (:category = '' OR category = :category) "+
            "AND quantity>0"+
            "order by brandPojo.brand,brandPojo.category";

    private static String SALES_REPORT= "SELECT brandPojo.brand AS brand, brandPojo.category AS category," +
            "SUM(orderItemPojo.quantity) AS quantity,SUM(orderItemPojo.sellingPrice) AS revenue "+
            "FROM OrderPojo orderPojo "+
            "INNER JOIN OrderItemPojo orderItemPojo ON orderPojo.id=orderItemPojo.orderId "+
            "INNER JOIN ProductPojo productPojo ON orderItemPojo.productId=productPojo.id "+
            "INNER JOIN BrandPojo brandPojo ON productPojo.brandCategory=brandPojo.id "+
            "WHERE (:brand = '' OR brand = :brand) " +
            "AND (:category = '' OR category = :category) "+
            "AND orderPojo.time BETWEEN :startDate AND :endDate "+
            "GROUP BY brandPojo.brand,brandPojo.category";


    public List<Tuple> getBrandReport(String brand, String category){
        TypedQuery<Tuple> query=em.createQuery(BRAND_REPORT,Tuple.class);
        query.setParameter("brand", brand != null && !brand.isEmpty() ? brand : "");
        query.setParameter("category", category != null && !category.isEmpty() ? category : "");

        List<Tuple> tuples= query.getResultList();
        return tuples;

    }

    public List<Tuple> getInventoryReport(String brand,String category){
        TypedQuery<Tuple> query=em.createQuery(INVENTORY_REPORT,Tuple.class);
        query.setParameter("brand", brand != null && !brand.isEmpty() ? brand : "");
        query.setParameter("category", category != null && !category.isEmpty() ? category : "");

        List<Tuple> tuples= query.getResultList();
        return tuples;

    }

    public List<Tuple> getSalesReport( LocalDateTime start, LocalDateTime end, String brand, String category) throws ApiException {
        TypedQuery<Tuple> query=em.createQuery(SALES_REPORT,Tuple.class);

        query.setParameter("brand", brand != null && !brand.isEmpty() ? brand : "");
        query.setParameter("category", category != null && !category.isEmpty() ? category : "");

        query.setParameter("startDate",start);
        query.setParameter("endDate",end);

        List<Tuple> tuples= query.getResultList();
        return tuples;

    }





}
