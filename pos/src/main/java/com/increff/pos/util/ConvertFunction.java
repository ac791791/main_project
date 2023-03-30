package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;


public class ConvertFunction {



    //BrandDto Converts
    public static BrandData convertBrandData(BrandPojo p) {
        BrandData d = new BrandData();
        d.setId(p.getId());
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        return d;
    }

    public static BrandPojo convertBrandPojo(BrandForm f) {
        BrandPojo p= new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

    // ProductDto converts
    public static ProductPojo convertProductPojo(ProductForm f) {
        ProductPojo p= new ProductPojo() ;
        p.setBarcode(f.getBarcode());
        p.setbrandCategory(f.getbrandCategory());
        p.setName(f.getName());

        double mrp = (f.getMrp()*100.0)/100.0;
//        double formattedMrp = Double.parseDouble(String.format("%.2f", mrp));
        p.setMrp(mrp);

        return p;
    }

    public static ProductData convertProductData(ProductPojo p) {

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

    public static InventoryPojo convertInventoryPojo(InventoryForm f) {
        InventoryPojo p = new InventoryPojo();
        p.setId(f.getId());
        p.setQuantity(f.getQuantity());
        return p;
    }

    public static InventoryData convertInventoryData(InventoryPojo p) {
        InventoryData d= new InventoryData();
        d.setId(p.getId());
        d.setQuantity(p.getQuantity());

        return d;
    }

    //Order Convert
    public static OrderData convertOrderData(OrderPojo p){
        OrderData d= new OrderData();
        d.setId(p.getId());
        d.setTime(p.getTime());
        d.setInvoiceStatus(p.getInvoiceStatus());
        return d;
    }

    //OrderItem Converts

    public static OrderItemData convertOrderItemData(OrderItemPojo p){
        OrderItemData d= new OrderItemData();
        d.setId(p.getId());
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        return d;
    }

    public static OrderItemPojo convertOrderItemPojo(OrderItemForm f) throws ApiException {


        OrderItemPojo p= new OrderItemPojo();
        p.setOrderId(f.getOrderId());
        p.setQuantity(f.getQuantity());

        double sellingPrice = f.getSellingPrice();
        double formattedSellingPrice = Double.parseDouble(String.format("%.2f", sellingPrice));
        p.setSellingPrice(formattedSellingPrice);

        return p;
    }

    //User Converts
    public static UserPojo convertUserPojo(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setRole("operator");
        p.setPassword(f.getPassword());
        return p;
    }
    public static UserData convertUserData(UserPojo p) {
        UserData d = new UserData();
        d.setEmail(p.getEmail());
        d.setRole(p.getRole());
        d.setId(p.getId());
        return d;
    }

    //Daily Report Convert
    public static DailyReportData convertDailyReportData(DailyReportPojo p){

        DailyReportData data=new DailyReportData();
        data.setDate(p.getDate());
        data.setTotalInvoice(p.getTotalInvoice());
        data.setTotalItems(p.getTotalItems());
        data.setTotalRevenue(p.getTotalRevenue());
        return data;
    }


}
