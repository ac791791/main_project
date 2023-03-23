package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;

	
	@Transactional
	public void add(InventoryPojo p) {
		dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}
	
	@Transactional
	public InventoryPojo get(int id) {
		return dao.select(id);
	}
	
//	@Transactional
//	public List<InventoryPojo> getAll(){
//		return dao.selectAll();
//	}
	@Transactional
	public List<InventoryPojo> getLimited(int page){
		return dao.selectLimited(page);
	}

	@Transactional
	public int totalInventory(){
		return dao.totalRows();
	}
	
	@Transactional
	public void update(int id, InventoryPojo p) throws ApiException {

		InventoryPojo updatedPojo = dao.select(id);
		updatedPojo.setQuantity(p.getQuantity());
	}
	@Transactional
	public void topUpdate(InventoryPojo p) throws ApiException {


		InventoryPojo updatedPojo = dao.select(p.getId());
		int quantity=updatedPojo.getQuantity()+p.getQuantity();
		updatedPojo.setQuantity(quantity);
	}

	public void decreaseInventory(InventoryPojo p, int quantity) throws ApiException {
		int inventoryQuantity=p.getQuantity();
		if(inventoryQuantity<quantity){
			throw new ApiException("Sorry, this much quantity is not present. Max Quantity: "+inventoryQuantity);
		}
		else
			p.setQuantity(inventoryQuantity-quantity);
	}

	public void increaseInventory(InventoryPojo p, int quantity){
		int inventoryQuantity=p.getQuantity();
		p.setQuantity(inventoryQuantity+quantity);
	}
}
