package com.increff.employee.util;

import com.increff.employee.model.*;
import com.increff.employee.pojo.*;
import com.increff.employee.service.ApiException;


public class ConvertFunction {



    //BrandDto Converts
    public static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setId(p.getId());
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        return d;
    }

    public static BrandPojo convert(BrandForm f) {
        BrandPojo p= new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

    // ProductDto converts
    public static ProductPojo productConvert(ProductForm f) {
        ProductPojo p= new ProductPojo() ;
        p.setBarcode(f.getBarcode());
        p.setbrandCategory(f.getbrandCategory());
        p.setName(f.getName());

        double mrp = f.getMrp();
        double formattedMrp = Double.parseDouble(String.format("%.2f", mrp));
        p.setMrp(formattedMrp);

        return p;
    }

    public static ProductData productConvert(ProductPojo p) {

        ProductData d= new ProductData();
        d.setId(p.getId());
        d.setBarcode(p.getBarcode());
        d.setbrandCategory(p.getbrandCategory());
        d.setName(p.getName());
        d.setMrp(p.getMrp());

        return d;
    }

    public static InventoryPojo createInventoryPojo(ProductPojo p){
        InventoryPojo inventoryPojo= new InventoryPojo();
        inventoryPojo.setId(p.getId());
        inventoryPojo.setQuantity(0);
        return inventoryPojo;
    }
    // Inventory Converts

    public static InventoryPojo inventoryConvert(InventoryForm f) {
        InventoryPojo p = new InventoryPojo();
        p.setId(f.getId());
        p.setQuantity(f.getQuantity());
        return p;
    }

    public static InventoryData inventoryConvert(InventoryPojo p) {
        InventoryData d= new InventoryData();
        d.setId(p.getId());
        d.setQuantity(p.getQuantity());

        return d;
    }

    //Order Convert
    public static OrderData orderConvert(OrderPojo p){
        OrderData d= new OrderData();
        d.setId(p.getId());
        d.setTime(p.getTime());
        d.setInvoiceStatus(p.getInvoiceStatus());
        return d;
    }

    //OrderItem Converts

    public static OrderItemData orderItemConvert(OrderItemPojo p){
        OrderItemData d= new OrderItemData();
        d.setId(p.getId());
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        return d;
    }

    public static OrderItemPojo orderItemConvert(OrderItemForm f) throws ApiException {


        OrderItemPojo p= new OrderItemPojo();
        p.setOrderId(f.getOrderId());
        p.setQuantity(f.getQuantity());

        double sellingPrice = f.getSellingPrice();
        double formattedSellingPrice = Double.parseDouble(String.format("%.2f", sellingPrice));
        p.setSellingPrice(formattedSellingPrice);

        return p;
    }

    //Daily Report Convert
    public static DailyReportData dailyReportConvert(DailyReportPojo p){

        DailyReportData data=new DailyReportData();
        data.setDate(p.getDate());
        data.setTotalInvoice(p.getTotalInvoice());
        data.setTotalItems(p.getTotalItems());
        data.setTotalRevenue(p.getTotalRevenue());
        return data;
    }

}
