package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.UserPojo;

import static com.increff.employee.util.Constants.pageRows;

@Repository
public class UserDao extends AbstractDao {

	private static String delete_id = "delete from UserPojo p where id=:id";
	private static String select_id = "select p from UserPojo p where id=:id";
	private static String select_email = "select p from UserPojo p where email=:email";
	private static String select_all = "select p from UserPojo p order by id desc";
	private static String total="Select count(*) from UserPojo p";

	
	@Transactional
	public void insert(UserPojo p) {
		em().persist(p);
	}

	public int delete(int id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public UserPojo select(int id) {
		TypedQuery<UserPojo> query = getQuery(select_id, UserPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(select_email, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	public List<UserPojo> selectAll() {
		TypedQuery<UserPojo> query = getQuery(select_all, UserPojo.class);
		return query.getResultList();
	}

	public List<UserPojo> selectLimited(int page) {
		TypedQuery<UserPojo> query = getQuery(select_all, UserPojo.class);
		query.setFirstResult(pageRows*(page-1));
		query.setMaxResults(pageRows);
		return query.getResultList();
	}

	public void update(UserPojo p) {
	}

	public int totalRows(){
		TypedQuery<Long> query=em.createQuery(total, Long.class);
		int rows=Integer.parseInt(String.valueOf(getSingle(query)));
		return rows;

	}

}