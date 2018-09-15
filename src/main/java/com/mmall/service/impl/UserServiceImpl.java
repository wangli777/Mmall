package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: WangLi
 * @Date: 2018/9/15 14:51
 * @Package: com.mmall.service.impl
 * @Description:
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username,String password) {
        //判断当前用户名是否存在
        if (userMapper.checkUserName(username)<=0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // todo MD5密码加密
        //判断密码是否正确
        User user = userMapper.login(username, password);

        if (user==null){
            //密码错误
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //将密码置为空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);

    }
}
