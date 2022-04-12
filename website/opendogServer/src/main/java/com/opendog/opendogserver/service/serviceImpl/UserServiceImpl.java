package com.opendog.opendogserver.service.serviceImpl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.opendog.opendogserver.common.CONSTANT;
import com.opendog.opendogserver.entity.User;
import com.opendog.opendogserver.mapper.UserMapper;
import com.opendog.opendogserver.service.UserService;
import com.opendog.opendogserver.utils.MD5Util;
import com.opendog.opendogserver.utils.TokenCacheUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;


import com.opendog.opendogserver.entity.User;
import com.opendog.opendogserver.mapper.UserMapper;
import com.opendog.opendogserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;


    //


    @Override
    public boolean insertUser(User user) {
        try{
            userMapper.insert(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean checkFiled(User user) {

        if(CONSTANT.USER_FIELDS.USERNAME.equals("username")) {
            long rows = userMapper.selectCount(Wrappers.<User>query().eq(CONSTANT.USER_FIELDS.USERNAME, user.getUserName()));
            if (rows > 0) {
                return false;
            }
        }else if(CONSTANT.USER_FIELDS.EMAIL.equals("email")){
            long rows =userMapper.selectCount(Wrappers.<User>query().eq(CONSTANT.USER_FIELDS.EMAIL,user.getEmail()));
            if(rows> 0){
                return false;
            }
        }
        return true;
    }

    @Override
    public User login(String userName, String password) {
        String md5Password = MD5Util.md5Encrypt32Upper(password);
        User loginUser = userMapper.selectOne(
                Wrappers.<User>query().eq("username",userName).eq("password",md5Password));
        if(loginUser == null){
            return null;
        }
        loginUser.setPassword("");
        return loginUser;


    }




    @Override
    public String getSafeQuestion(String userName) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName);

        String question = userMapper.selectOne(Wrappers.<User>query().eq("username",userName)).getQuestion();
        if(!question.equals("")){
            return question;
        }


        return null;
    }

    @Override
    public boolean checkQuestionAnswer(String userName, String answer) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName).eq("answer",answer);
        long rows = userMapper.selectCount(queryWrapper);
        if(rows > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCacheUtil.setToken(userName, forgetToken);
            System.out.println(userName+":"+forgetToken);
            return true;
        }


        return false;
    }

    @Override
    public boolean resetForgetPassword(String userName, String newPassword) {

        String token = TokenCacheUtil.getToken(userName);
        if(!token.equals("")){
            return false;
        }
        String forgetToken="";
        if(token.equals( forgetToken)){
            String md5Password = MD5Util.md5Encrypt32Upper(newPassword);
            User user = new User();
            user.setUserName(userName);
            user.setPassword(md5Password);

            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username", userName);
            updateWrapper.set("password", user.getPassword());
            int rows = userMapper.update(user, updateWrapper);

            if(rows > 0){
                return true;
            }
            return false;
        }else{
            return false;
        }



    }


    @Override
    public boolean resetPassword(String userName, String oldPassword, String newPassword) {

        User user=new User();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",user.getUid());
        queryWrapper.eq("password", MD5Util.md5Encrypt32Upper(oldPassword));
        long rows = userMapper.selectCount(queryWrapper);
        if(rows == 0){
            return false;
        }

        user.setPassword(MD5Util.md5Encrypt32Upper(newPassword));

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",user.getUid());
        updateWrapper.set("password",user.getPassword());

        rows = userMapper.update(user,updateWrapper);

        if(rows > 0 ){
            return true;
        }


        return false;
    }

    @Override
    public User getUserDetail(int uid) {

        User userDetail = userMapper.selectById(uid);
        if(userDetail == null){
            return null;
        }
        userDetail.setPassword("");
        return userDetail;
    }

    @Override
    public User updateUserDetail(String userName, String password, String email, String question, String answer) {
        User user=new User();
        user.setUpdatedTime(new Date(System.currentTimeMillis()));
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",user.getUid());
        updateWrapper.set("email",user.getEmail());
        updateWrapper.set("question", user.getQuestion());
        updateWrapper.set("answer", user.getAnswer());
        updateWrapper.set("update_time", user.getUpdatedTime());
        long rows = userMapper.update(user,updateWrapper);
        if(rows > 0){
            return user;
        }

        return null;
    }

}
