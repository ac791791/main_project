package com.increff.employee.dao;

import javax.transaction.Transactional;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.InventoryPojo;

import static com.increff.employee.util.Constants.pageRows;

@Repository
public class InventoryDao extends AbstractDao {

	private static String delete_id ="delete from InventoryPojo p where id=:id";
	private static String select_id ="select p from InventoryPojo p where id=:id";
	private static String select_all="select p from InventoryPojo p order by id desc";
	private static String total_rows="Select count(*) from InventoryPojo p";
	
	@Transactional
	public void insert(InventoryPojo p) {
		em.persist(p);
	}
	
	public void delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id",id);
		query.executeUpdate();
		
	}
	
	public InventoryPojo select(int id) {
		TypedQuery<InventoryPojo> query= em.createQuery(select_id, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
//	public List<InventoryPojo> selectAll(){
//		TypedQuery<InventoryPojo> query= em.createQuery(select_all, InventoryPojo.class);
//		return query.getResultList();
//		}
	public List<InventoryPojo> selectLimited(int page){
		TypedQuery<InventoryPojo> query= em.createQuery(select_all, InventoryPojo.class);
		query.setFirstResult(pageRows*(page-1));
		query.setMaxResults(pageRows);
		return query.getResultList();
	}

	public int totalRows(){
		TypedQuery<Long> query=em.createQuery(total_rows, Long.class);
		int rows=Integer.parseInt(String.valueOf(getSingle(query)));
		return rows;

	}


}
