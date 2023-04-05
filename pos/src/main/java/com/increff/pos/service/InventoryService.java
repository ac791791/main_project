package com.increff.pos.service;

import java.util.List;
import java.util.Objects;

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
	

	public void update(int id, int quantity) throws ApiException {
		InventoryPojo updatedPojo = dao.select(id);
		if(Objects.isNull(updatedPojo))
			throw new ApiException("Product with given id "+id+" does not exist");
		updatedPojo.setQuantity(quantity);
	}

	public void topUpdate(InventoryPojo p) throws ApiException {
		InventoryPojo updatedPojo = dao.select(p.getId());
		int quantity = updatedPojo.getQuantity() + p.getQuantity();
		updatedPojo.setQuantity(quantity);
	}

	public void decreaseInventory(ProductPojo productPojo, int quantity) throws ApiException {
		InventoryPojo p = get(productPojo.getId());
		int inventoryQuantity = p.getQuantity();
		if(inventoryQuantity < quantity)
			throw new ApiException(quantity+" quantity is not present. Max Quantity: "
					+ inventoryQuantity);

		else
			p.setQuantity(inventoryQuantity-quantity);
	}

	public void increaseInventory(int id, int quantity){
		InventoryPojo pojo = get(id);
		int inventoryQuantity = pojo.getQuantity();
		pojo.setQuantity(inventoryQuantity + quantity);
	}
}
