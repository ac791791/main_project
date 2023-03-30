package com.increff.pos.dao;

import java.util.List;
import javax.transaction.Transactional;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

import static com.increff.pos.util.Constants.pageSize;


@Repository
public class BrandDao extends AbstractDao {


	private static String SELECT_ID ="select p from BrandPojo p where id=:id";
	private static String SELECT_ALL="select p from BrandPojo p order by id desc";
	private static String SELECT_BRAND_CATEGORY="select p from BrandPojo p where brand=:brand AND category=:category";
	private static String TOTAL_ROWS="Select count(*) from BrandPojo p";
	
	@Transactional
	public void insert(BrandPojo p) {
		em.persist(p);
	}

	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query= em.createQuery(SELECT_ID,BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	// Used in checking unique Brand and Category combination
	public BrandPojo selectByBrandCategory(String brand,String category){
		TypedQuery<BrandPojo> query=em.createQuery(SELECT_BRAND_CATEGORY,BrandPojo.class);
		query.setParameter("brand",brand);
		query.setParameter("category",category);
		return  getSingle(query);
	}

	//TODO: to move this to abstract dao
	public List<BrandPojo> selectAll(){
		TypedQuery<BrandPojo> query=em.createQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}


	// Used to get only limited records for a particular page
	public List<BrandPojo> selectLimited(int pageNo){
		TypedQuery<BrandPojo> query= em.createQuery(SELECT_ALL, BrandPojo.class);
		query.setFirstResult(pageSize*(pageNo-1));
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	// Used in getting total records
	public int totalRows(){
		TypedQuery<Long> query=em.createQuery(TOTAL_ROWS, Long.class);
		int rows=Integer.parseInt(String.valueOf(getSingle(query)));
		return rows;

	}


	
}
