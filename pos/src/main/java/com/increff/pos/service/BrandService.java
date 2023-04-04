package com.increff.pos.service;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;



@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {
	
	@Autowired
	private BrandDao dao;


	public void add(BrandPojo p) throws ApiException {
		BrandPojo existingPojo = dao.selectByBrandCategory(p.getBrand(),p.getCategory());
		if(Objects.nonNull(existingPojo))
			throw new ApiException("Couldn't Add: Given Brand Category already exist, brand: " + existingPojo.getBrand());
		dao.insert(p);
	}

	public BrandPojo get(int id) {
		return dao.select(id);
	}


	public BrandPojo getByBrandCategory(String brand, String category){
		return dao.selectByBrandCategory(brand,category);
	}

	public List<BrandPojo> getAll(){
		return dao.selectAll();
	}


	public  List<BrandPojo> getLimited(int pageNo){
		return dao.selectLimited(pageNo);
	}


	public int totalBrands(){
		return dao.totalRows();
	}


	public void update(int id, BrandPojo p) throws ApiException {

		BrandPojo existingPojo = dao.selectByBrandCategory(p.getBrand(),p.getCategory());
		if(existingPojo!= null && existingPojo.getId()!=id)
			throw new ApiException("Couldn't Update: Given Brand Category already exist");

		BrandPojo updatedPojo = dao.select(id);
		updatedPojo.setBrand(p.getBrand());
		updatedPojo.setCategory(p.getCategory());
	}


}
