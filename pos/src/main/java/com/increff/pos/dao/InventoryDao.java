package com.increff.pos.dao;

import javax.transaction.Transactional;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;

import static com.increff.pos.util.Constants.pageSize;

@Repository
public class InventoryDao extends AbstractDao {

	private static String SELECT_ID ="select p from InventoryPojo p where id=:id";
	private static String SELECT_ALL="select p from InventoryPojo p order by id desc";
	private static String TOTAL_ROWS="Select count(*) from InventoryPojo p";
	
	@Transactional
	public void insert(InventoryPojo p) {
		em.persist(p);
	}

	public InventoryPojo select(int id) {
		TypedQuery<InventoryPojo> query = em.createQuery(SELECT_ID, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	public List<InventoryPojo> selectAll(){
		TypedQuery<InventoryPojo> query = em.createQuery(SELECT_ALL, InventoryPojo.class);
		return query.getResultList();
		}
	public List<InventoryPojo> selectLimited(int pageNo){
		TypedQuery<InventoryPojo> query = em.createQuery(SELECT_ALL, InventoryPojo.class);
		query.setFirstResult(pageSize*(Math.max(pageNo-1,0)));
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public int totalRows(){
		TypedQuery<Long> query = em.createQuery(TOTAL_ROWS, Long.class);
		int rows = Integer.parseInt(String.valueOf(getSingle(query)));
		return rows;
	}
}
