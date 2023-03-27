package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.BrandPojo;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;


	
	@Transactional
	public void add(ProductPojo p) throws ApiException {

		ProductPojo pojo= dao.select(p.getBarcode());
		if(pojo==null) {
			dao.insert(p);

		}
		else {
			throw new ApiException("Couldn't Add: Given Barcode already exist");
		}
	}

	
	@Transactional
	public ProductPojo get(int id) {
		return dao.select(id);
	}

	@Transactional
	public ProductPojo get(String barcode) {
		return dao.select(barcode);
	}

	@Transactional
	public List<ProductPojo> getAll(){
		return dao.selectAll();
	}

	@Transactional
	public  List<ProductPojo> getLimited(int page){
		return dao.selectLimited(page);
	}

	@Transactional
	public int totalProducts(){
		return dao.totalRows();
	}

//	@Transactional
//	public List<ProductPojo> getByBrandCategory(int brandCategory ){
//		return dao.selectByBrandCategory(brandCategory);
//	}
	
	@Transactional
	public void update(int id, ProductPojo p) throws ApiException {

		ProductPojo updatedPojo = dao.select(id);
		updatedPojo.setName(p.getName());
		updatedPojo.setMrp(p.getMrp());
	}


}
