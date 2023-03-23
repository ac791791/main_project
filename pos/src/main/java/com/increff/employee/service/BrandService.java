package com.increff.employee.service;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;


@Service
public class BrandService {
	
	@Autowired
	private BrandDao dao;

	
	@Transactional
	public void add(BrandPojo p) throws ApiException {

		BrandPojo pojo=dao.selectByBrandCategory(p.getBrand(),p.getCategory());
		if(pojo==null) {
			dao.insert(p);
		}
		else {
			throw new ApiException("Couldn't Add: Given Brand Category already exist");
		}

	}

	@Transactional
	public BrandPojo get(int id) {
		return dao.select(id);
	}

	@Transactional
	public BrandPojo getByBrandCategory(String brand, String category){
		return dao.selectByBrandCategory(brand,category);
	}
	@Transactional
	public List<BrandPojo> getAll(){
		return dao.selectAll();
	}


	@Transactional
	public  List<BrandPojo> getLimited(int page){
		return dao.selectLimited(page);
	}
	@Transactional
	public int totalBrands(){
		return dao.totalRows();
	}
	
	@Transactional
	public void update(int id, BrandPojo p) throws ApiException {

		BrandPojo pojo=dao.selectByBrandCategory(p.getBrand(),p.getCategory());
		if(pojo==null) {
			BrandPojo updatedPojo= dao.select(id);
			updatedPojo.setBrand(p.getBrand());
			updatedPojo.setCategory(p.getCategory());

		} else if (pojo.getId()==id) {
			
		} else {
			throw new ApiException("Couldn't Update: Given Brand Category already exist");
		}
	}


}
