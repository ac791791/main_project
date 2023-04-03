package com.increff.pos.dto;

import com.increff.pos.model.ChangePasswordForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.util.Constants.pageSize;
import static org.junit.Assert.assertEquals;

public class UserDtoTest extends AbstractUnitTest{

    @Autowired
    private UserDto dto;
    @Before
    public void setUp() throws ApiException {
        UserForm form=new UserForm();
        form.setEmail("o1@gmail.com");
        form.setPassword("password");
        dto.add(form);
    }

    @Test
    public void testAdd() throws ApiException {
        UserForm form=new UserForm();
        form.setEmail("s1@gmail.com");
        form.setPassword("password");
        dto.add(form);
    }

    @Test
    public void testGetAll() throws ApiException {
        List<UserData> list1=dto.getAll();
        assertEquals(1,list1.size());

        UserForm form=new UserForm();
        form.setEmail("s1@gmail.com");
        form.setPassword("password");
        dto.add(form);
        List<UserData> list2=dto.getAll();
        assertEquals(2,list2.size());
    }

    @Test
    public void testGetLimited() throws ApiException {
        for(int i=0;i<15;i++){
            UserForm form=new UserForm();
            form.setEmail(i+"s1@gmail.com");
            form.setPassword(i+"password");
            dto.add(form);
        }
        assertEquals(pageSize,dto.getLimited(1).size());
        assertEquals(16-pageSize,dto.getLimited(2).size());
    }

    @Test
    public void testTotalUsers() throws ApiException {
        assertEquals(1,dto.totalUsers());
        UserForm form=new UserForm();
        form.setEmail("s1@gmail.com");
        form.setPassword("password");
        dto.add(form);

        assertEquals(2,dto.totalUsers());
    }

    @Test
    public void testDelete(){
        List<UserData> list=dto.getAll();

        for(UserData data:list){
            dto.delete(data.getId());
        }
        assertEquals(0,dto.getAll().size());

    }

    @Test
    public void testChangePassword() throws ApiException {
        List<UserData> userDataList=dto.getAll();

        for (UserData userData: userDataList){
            ChangePasswordForm changePasswordForm= new ChangePasswordForm();
            changePasswordForm.setEmail(userData.getEmail());
            changePasswordForm.setCurrentPassword("password");
            changePasswordForm.setNewPassword("123");
            changePasswordForm.setConfirmPassword("123");
            dto.changePassword(changePasswordForm);
        }
    }


    @Test
    public void testWrongCurrentPasswordChangePassword() throws ApiException {
        List<UserData> userDataList=dto.getAll();

        for (UserData userData: userDataList){
            ChangePasswordForm changePasswordForm= new ChangePasswordForm();
            changePasswordForm.setEmail(userData.getEmail());
            changePasswordForm.setCurrentPassword("password1");
            changePasswordForm.setNewPassword("123");
            changePasswordForm.setConfirmPassword("123");
            try {
                dto.changePassword(changePasswordForm);
            }
            catch (ApiException e){
                assertEquals("Current password is Wrong",e.getMessage());
            }
        }
    }

    @Test
    public void testDifferentConfirmPasswordChangePassword() throws ApiException {
        List<UserData> userDataList=dto.getAll();

        for (UserData userData: userDataList){
            ChangePasswordForm changePasswordForm= new ChangePasswordForm();
            changePasswordForm.setEmail(userData.getEmail());
            changePasswordForm.setCurrentPassword("password");
            changePasswordForm.setNewPassword("123");
            changePasswordForm.setConfirmPassword("1234");
            try {
                dto.changePassword(changePasswordForm);
            }
            catch (ApiException e){
                assertEquals("Confirm Password didn't Matched",e.getMessage());
            }
        }
    }
}