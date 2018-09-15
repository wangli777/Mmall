package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @Author: WangLi
 * @Date: 2018/9/13 13:07
 * @Package: com.mmall.service
 * @Description:
 */
public interface IUserService {
    ServerResponse<User> login(String username,String password);
}
