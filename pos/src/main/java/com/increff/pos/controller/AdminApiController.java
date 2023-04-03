package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.increff.pos.dto.UserDto;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/users")
public class AdminApiController {


	@Autowired
	private UserDto dto;

	@ApiOperation(value = "Adds a user")
	@RequestMapping(method = RequestMethod.POST)
	public void addUser(@RequestBody UserForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) {
		dto.delete(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping( method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		return dto.getAll();
	}


	@ApiOperation(value = "Gets list of users of a given page")
	@RequestMapping(value = "/getLimited/{pageNo}", method = RequestMethod.GET)
	public List<UserData> getLimitedUser(@PathVariable int pageNo) throws ApiException {
		return dto.getLimited(pageNo);
	}


	@ApiOperation(value = "Getting total no of Users")
	@RequestMapping(value = "/total", method = RequestMethod.GET)
	public int totalUsers(){
		return dto.totalUsers();
	}


	@ApiOperation(value ="Changing password")
	@RequestMapping( method = RequestMethod.PUT)
	public void changePassword(@RequestBody ChangePasswordForm form) throws ApiException {
			dto.changePassword(form);
	}




}
