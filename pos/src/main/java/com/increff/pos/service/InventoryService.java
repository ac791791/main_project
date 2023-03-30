package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

	@Autowired
	private InventoryDao dao;


	public void add(InventoryPojo p) {
		dao.insert(p);
	}

	public InventoryPojo get(int id) {
		return dao.select(id);
	}
	

	public List<InventoryPojo> getAll(){
		return dao.selectAll();
	}


	public List<InventoryPojo> getLimited(int pageNo){
		return dao.selectLimited(pageNo);
	}


	public int totalInventory(){
		return dao.totalRows();
	}
	

	public void update(int id, InventoryPojo p) throws ApiException {
		InventoryPojo updatedPojo = dao.select(id);
		updatedPojo.setQuantity(p.getQuantity());
	}

	public void topUpdate(InventoryPojo p) throws ApiException {
		InventoryPojo updatedPojo = dao.select(p.getId());
		int quantity = updatedPojo.getQuantity() + p.getQuantity();
		updatedPojo.setQuantity(quantity);
	}

	public void decreaseInventory(InventoryPojo p, int quantity) throws ApiException {
		int inventoryQuantity = p.getQuantity();
		if(inventoryQuantity < quantity)
			throw new ApiException("Sorry, this much quantity is not present. Max Quantity: "
					+ inventoryQuantity);

		else
			p.setQuantity(inventoryQuantity-quantity);
	}

	public void increaseInventory(InventoryPojo p, int quantity){
		int inventoryQuantity=p.getQuantity();
		p.setQuantity(inventoryQuantity+quantity);
	}
}
