//package com.increff.pos.dao;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.stereotype.Repository;
//
//import com.increff.pos.pojo.posPojo;
//
//@Repository
//public class posInMemDao {
//
//	private HashMap<Integer, posPojo> rows;
//	private int lastId;
//
//	@PostConstruct
//	public void init() {
//		rows = new HashMap<Integer, posPojo>();
//	}
//
//	public void insert(posPojo p) {
//		lastId++;
//		p.setId(lastId);
//		rows.put(lastId, p);
//	}
//
//	public void delete(int id) {
//		rows.remove(id);
//	}
//
//	public posPojo select(int id) {
//		return rows.get(id);
//	}
//
//	public List<posPojo> selectAll() {
//		ArrayList<posPojo> list = new ArrayList<posPojo>();
//		list.addAll(rows.values());
//		return list;
//	}
//
//	public void update(int id, posPojo p) {
//		rows.put(id, p);
//	}
//
//}
