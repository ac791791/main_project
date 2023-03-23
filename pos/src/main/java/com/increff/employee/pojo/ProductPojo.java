package com.increff.employee.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(indexes = {@Index(columnList = "barcode,brandCategory,name,mrp")},
uniqueConstraints = {@UniqueConstraint(columnNames = ("barcode"))}, name = "products")

public class
ProductPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String barcode;
	@Column(nullable = false)
	private int brandCategory;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private double mrp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public int getbrandCategory() {
		return brandCategory;
	}
	public void setbrandCategory(int brandCategory) {
		this.brandCategory = brandCategory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	
	
	
	
}
