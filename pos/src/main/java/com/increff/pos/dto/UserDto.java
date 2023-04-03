package com.increff.pos.dto;

import com.increff.pos.model.ChangePasswordForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.util.ConvertFunction.*;
import static com.increff.pos.util.InputChecks.validateUserForm;

@Repository
public class UserDto {

    @Autowired
    private UserService service;

    public void add(UserForm form) throws ApiException {
        validateUserForm(form);
        UserPojo pojo = convertUserPojo(form);
        service.add(pojo);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public List<UserData> getAll() {
        List<UserPojo> userPojoList = service.getAll();
        List<UserData> userDataList = new ArrayList<UserData>();
        for (UserPojo pojo : userPojoList) {
            userDataList.add(convertUserData(pojo));
        }
        return userDataList;
    }

    public List<UserData> getLimited(int pageNo) throws ApiException {
        if(pageNo<1)
            throw new ApiException("Page No can't be less than 1");
        List<UserPojo> userPojoList = service.getLimited(pageNo);
        List<UserData> userDataList = new ArrayList<UserData>();
        for (UserPojo pojo : userPojoList) {
            userDataList.add(convertUserData(pojo));
        }
        return userDataList;
    }

    public int totalUsers(){
        return service.totalUsers();
    }

    public void changePassword(ChangePasswordForm form) throws ApiException {

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
            throw new ApiException("Current password is Wrong");
        }
    }



}
