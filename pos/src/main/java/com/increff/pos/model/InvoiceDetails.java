package com.increff.pos.model;


import java.time.LocalDateTime;
import java.util.List;


public class InvoiceDetails {

    private List<InvoiceItem> items;
    private Integer orderId;

    private LocalDateTime time;

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }




}
