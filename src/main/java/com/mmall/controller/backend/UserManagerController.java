package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: WangLi
 * @Date: 2018/9/15 22:30
 * @Package: com.mmall.controller.backend
 * @Description: 后台管理员相关Controller
 */
@RequestMapping("/user/manager")
public class UserManagerController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            User user = response.getData();
            if (Const.Role.ROLE_ADMIN == user.getRole()) {
                //是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else {
                //不是管理员
                return ServerResponse.createByErrorMessage("您不是管理员，无法登录");
            }
        }
        return response;
    }
}
