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

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String field, String type);

    ServerResponse<String> getQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(User user, String passwordNew, String passwordOld);

    ServerResponse<User> updateInfo(User user);

    ServerResponse<User> getInfo(Integer id);
}
