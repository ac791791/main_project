package com.increff.pos.pojo;


import javax.persistence.*;

@Entity
@Table(
        name = "daily_report"
)
public class DailyReportPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String date;
    private int totalInvoice;
    private int totalItems;

    private double totalRevenue;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(int totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }


}