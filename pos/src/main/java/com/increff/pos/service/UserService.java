package com.increff.pos.service;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

import static com.increff.pos.util.NormalizeFunctions.normalize;

@Service
@Transactional(rollbackOn = ApiException.class)
public class UserService {

	@Autowired
	private UserDao dao;
	@Value("${supervisor.list}")
	private String supervisorList;


	public void add(UserPojo p) throws ApiException {
		normalize(p);

		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(assignRole(p));
	}

	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}


	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	public List<UserPojo> getLimited(int pageNo) {
		return dao.selectLimited(pageNo);
	}

	public void delete(int id) {
		dao.delete(id);
	}


	public void changePassword(String email, String password){
		UserPojo updatedPojo= dao.select(email);
		updatedPojo.setPassword(password);
	}

	private UserPojo assignRole(UserPojo p){
		List<String> stringList = Arrays.asList(supervisorList.split(","));

		for(String s:stringList)
		{
			String s1=p.getEmail();
			if(s1.equals(s))
			{
				p.setRole("supervisor");
			}
		}
		return p;

	}

	public int totalUsers(){
		return dao.totalRows();
	}



}
