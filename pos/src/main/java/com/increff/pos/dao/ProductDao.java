package com.increff.pos.dao;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.increff.pos.pojo.ProductPojo;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import static com.increff.pos.util.Constants.pageSize;

@Repository
public class ProductDao extends AbstractDao {

	private static String SELECT_ID="select p from ProductPojo p where id=:id";
	private static String SELECT_ALL="select p from ProductPojo p order by id desc";
	private static  String SELECT_BARCODE="select p from ProductPojo p where barcode=:barcode";

//	private static String select_brandCategory="select p from ProductPojo p where brandCategory=:brandCategory";

	private static String TOTAL_ROWS="Select count(*) from ProductPojo p";


	
	@Transactional
	public void insert(ProductPojo p) {
		em.persist(p);
	}


	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = em.createQuery(SELECT_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	public ProductPojo select(String barcode) {
		TypedQuery<ProductPojo> query = em.createQuery(SELECT_BARCODE, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}
	public List<ProductPojo> selectAll(){
		TypedQuery<ProductPojo> query= em.createQuery(SELECT_ALL, ProductPojo.class);


		return query.getResultList();
	}

	public List<ProductPojo> selectLimited(int pageNo){
		TypedQuery<ProductPojo> query= em.createQuery(SELECT_ALL, ProductPojo.class);
		query.setFirstResult(pageSize*(pageNo-1));
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public int totalRows(){
		TypedQuery<Long> query=em.createQuery(TOTAL_ROWS, Long.class);
		int rows=Integer.parseInt(String.valueOf(getSingle(query)));
		return rows;

	}
//	public List<ProductPojo> selectByBrandCategory(int brandCategory){
//		TypedQuery<ProductPojo> query = em.createQuery(select_brandCategory, ProductPojo.class);
//		query.setParameter("brandCategory",brandCategory);
//		return query.getResultList();
//	}

}
