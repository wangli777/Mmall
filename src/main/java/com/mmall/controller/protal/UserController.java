package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: WangLi
 * @Date: 2018/9/13 13:05
 * @Package: com.mmall.controller.protal
 * @Description: 用户Controller
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    IUserService iUserService;

    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //登录成功 将用户信息存储到Session
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

}
