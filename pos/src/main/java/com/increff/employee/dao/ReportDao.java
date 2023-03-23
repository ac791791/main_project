package com.increff.employee.dao;

import com.increff.employee.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import static com.increff.employee.util.StringUtil.isEmpty;

@Repository
public class ReportDao extends AbstractDao{

    private static String brandReport=" SELECT brand AS brand, category AS category from BrandPojo "+
            "WHERE (:brand = '' OR brand = :brand) " +
            "AND (:category = '' OR category = :category)";

    private static String inventoryReport="Select brandPojo.brand AS brand, brandPojo.category AS category,"+
            "productPojo.barcode AS barcode, productPojo.name AS name,inventoryPojo.quantity AS quantity "+
            "FROM BrandPojo brandPojo "+
            "INNER JOIN ProductPojo productPojo ON brandPojo.id=productPojo.brandCategory "+
            "INNER JOIN InventoryPojo inventoryPojo ON productPojo.id=inventoryPojo.id "+
            "WHERE (:brand = '' OR brand = :brand) " +
            "AND (:category = '' OR category = :category) "+
            "AND quantity>0"+
            "order by brandPojo.brand,brandPojo.category";

    private static String salesReport= "SELECT brandPojo.brand AS brand, brandPojo.category AS category," +
            "SUM(orderItemPojo.quantity) AS quantity,SUM(orderItemPojo.sellingPrice) AS revenue "+
            "FROM OrderPojo orderPojo "+
            "INNER JOIN OrderItemPojo orderItemPojo ON orderPojo.id=orderItemPojo.orderId "+
            "INNER JOIN ProductPojo productPojo ON orderItemPojo.productId=productPojo.id "+
            "INNER JOIN BrandPojo brandPojo ON productPojo.brandCategory=brandPojo.id "+
            "WHERE (:brand = '' OR brand = :brand) " +
            "AND (:category = '' OR category = :category) "+
            "AND orderPojo.time BETWEEN :startDate AND :endDate "+
            "GROUP BY brandPojo.brand,brandPojo.category";




    public List<SalesReportData> getSalesReport(SalesReportForm form){
        TypedQuery<Tuple> query=em.createQuery(salesReport,Tuple.class);

        LocalDateTime end;
        LocalDateTime start;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(isEmpty(form.getStartDate())){
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(365);
        }
        else {
            LocalDate startDate = LocalDate.parse(form.getStartDate(), formatter);
            start = startDate.atTime(LocalTime.MIN);
        }
        if(isEmpty(form.getEndDate())) {
            end=java.time.LocalDateTime.now();

        }
        else{
            LocalDate endDate = LocalDate.parse(form.getEndDate(), formatter);
            end = endDate.atTime(LocalTime.MAX);
        }

        String brand = form.getBrand();
        String category = form.getCategory();

        query.setParameter("brand", brand != null && !brand.isEmpty() ? brand : "");
        query.setParameter("category", category != null && !category.isEmpty() ? category : "");

        query.setParameter("startDate",start);
        query.setParameter("endDate",end);

        List<Tuple> tuples= query.getResultList();
        return salesConvert(tuples);


    }

    public List<BrandReportData> getBrandReport(BrandReportForm form){
        TypedQuery<Tuple> query=em.createQuery(brandReport,Tuple.class);

        String brand = form.getBrand();
        String category = form.getCategory();

        query.setParameter("brand", brand != null && !brand.isEmpty() ? brand : "");
        query.setParameter("category", category != null && !category.isEmpty() ? category : "");

        List<Tuple> tuples= query.getResultList();
        return brandConvert(tuples);

    }

    public List<InventoryReportData> getInventoryReport(InventoryReportForm form){
        TypedQuery<Tuple> query=em.createQuery(inventoryReport,Tuple.class);

        String brand = form.getBrand();
        String category = form.getCategory();

        query.setParameter("brand", brand != null && !brand.isEmpty() ? brand : "");
        query.setParameter("category", category != null && !category.isEmpty() ? category : "");

        List<Tuple> tuples= query.getResultList();
        return inventoryConvert(tuples);

    }


    private List<SalesReportData> salesConvert(List<Tuple> tuples){
        List<SalesReportData> list= new ArrayList<SalesReportData>();

        for(Tuple tuple:tuples){
            SalesReportData data= new SalesReportData();
            data.setBrand(String.valueOf(tuple.get("brand")));
            data.setCategory(String.valueOf(tuple.get("category")));
            data.setQuantity(Integer.parseInt(tuple.get("quantity").toString()));
            data.setRevenue(Double.parseDouble(tuple.get("revenue").toString()));
            list.add(data);
        }
        return list;
    }

    private List<BrandReportData> brandConvert(List<Tuple> tuples){
        List<BrandReportData> list=new ArrayList<BrandReportData>();

        for(Tuple tuple:tuples){
            BrandReportData data= new BrandReportData();
            data.setBrand(String.valueOf(tuple.get("brand")));
            data.setCategory(String.valueOf(tuple.get("category")));
            list.add(data);
        }
        return list;

    }

    private List<InventoryReportData> inventoryConvert(List<Tuple> tuples){
        List<InventoryReportData> list= new ArrayList<InventoryReportData>();

        for(Tuple tuple:tuples){
            InventoryReportData data= new InventoryReportData();
            data.setBrand(String.valueOf(tuple.get("brand")));
            data.setCategory(String.valueOf(tuple.get("category")));
            data.setQuantity(Integer.parseInt(tuple.get("quantity").toString()));
            data.setBarcode(String.valueOf(tuple.get("barcode")));
            data.setName(String.valueOf(tuple.get("name")));
            list.add(data);
        }
        return list;
    }


}
