package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.increff.pos.model.ChangePasswordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class AdminApiController {

	@Autowired
	private UserService service;

	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/api/user", method = RequestMethod.POST)
	public void addUser(@RequestBody UserForm form) throws ApiException {
		UserPojo p = convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(path = "/api/user/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/api/user", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> list = service.getAll();
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Gets list of users of a given page")
	@RequestMapping(path = "/api/user/getLimited/{page}", method = RequestMethod.GET)
	public List<UserData> getLimitedUser(@PathVariable int page) {
		List<UserPojo> list = service.getLimited(page);
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}


	@ApiOperation(value = "Getting total no of Users")
	@RequestMapping(path = "/api/user/totalUsers", method = RequestMethod.GET)
	public int totalUsers(){
		return service.totalUsers();
	}

	@ApiOperation(value ="Changing password")
	@RequestMapping(path = "/api/user", method = RequestMethod.PUT)
	public void changePassword(@RequestBody ChangePasswordForm form) throws ApiException {

			UserPojo pojo= service.get(form.getEmail());
			if(Objects.equals(pojo.getPassword(), form.getCurrentPassword())){
				if(Objects.equals(form.getNewPassword(), form.getConfirmPassword())){
					service.changePassword(form.getEmail(), form.getNewPassword());
				}
				else{
					throw new ApiException("Confirm Password didn't Matched");
				}

			}
			else{
				throw new ApiException("Current password is Wrong.");
			}
		}


	private static UserData convert(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		return d;
	}

	private static UserPojo convert(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setRole("operator");
		p.setPassword(f.getPassword());
		return p;
	}

}
