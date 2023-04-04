package com.increff.pos.service;

import java.util.List;
import java.util.Objects;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {

	@Autowired
	private ProductDao dao;

	public void add(ProductPojo p) throws ApiException {
		ProductPojo existingPojo = dao.select(p.getBarcode());
		if(Objects.nonNull(existingPojo))
			throw new ApiException("Couldn't Add: Given Barcode already exist");
		dao.insert(p);
	}

	public ProductPojo get(int id) {
		return dao.select(id);
	}

	public ProductPojo getCheck(String barcode) throws ApiException {
		ProductPojo pojo = dao.select(barcode);
		if(Objects.isNull(pojo))
			throw new ApiException("Sorry, "+barcode+" is not present.");
		return dao.select(barcode);
	}

	public List<ProductPojo> getAll(){
		return dao.selectAll();
	}

	public  List<ProductPojo> getLimited(int pageNo) {
		return dao.selectLimited(pageNo);
	}

	public int totalProducts(){
		return dao.totalRows();
	}

	public void update(int id, String name, double mrp) throws ApiException {
		ProductPojo updatedPojo = dao.select(id);
		if(Objects.isNull(updatedPojo))
			throw new ApiException("Product with given id "+ id+" doees not exist");
		updatedPojo.setName(name);
		updatedPojo.setMrp(mrp);
	}

}
