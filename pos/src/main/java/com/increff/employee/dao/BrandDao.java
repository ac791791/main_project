package com.increff.employee.dao;

import java.util.List;
import javax.transaction.Transactional;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.increff.employee.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandPojo;

import static com.increff.employee.util.Constants.pageRows;


@Repository
public class BrandDao extends AbstractDao {


	private static String select_id ="select p from BrandPojo p where id=:id";
	private static String select_all="select p from BrandPojo p order by id desc";
	private static String select_brand_category="select p from BrandPojo p where brand=:brand AND category=:category";

	private static String total_rows="Select count(*) from BrandPojo p";
	
	@Transactional
	public void insert(BrandPojo p) {
		em.persist(p);
	}
	

	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query= em.createQuery(select_id,BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// Used in checking unique Brand and Category combination
	public BrandPojo selectByBrandCategory(String brand,String category){
		TypedQuery<BrandPojo> query=em.createQuery(select_brand_category,BrandPojo.class);
		query.setParameter("brand",brand);
		query.setParameter("category",category);
		return  getSingle(query);
	}
	
	public List<BrandPojo> selectAll(){
		TypedQuery<BrandPojo> query=em.createQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}


	// Used to get only limited records for a particular page
	public List<BrandPojo> selectLimited(int page){
		TypedQuery<BrandPojo> query= em.createQuery(select_all, BrandPojo.class);
		query.setFirstResult(pageRows*(page-1));
		query.setMaxResults(pageRows);
		return query.getResultList();
	}

	// Used in getting total records
	public int totalRows(){
		TypedQuery<Long> query=em.createQuery(total_rows, Long.class);
		int rows=Integer.parseInt(String.valueOf(getSingle(query)));
		return rows;

	}


	
}
