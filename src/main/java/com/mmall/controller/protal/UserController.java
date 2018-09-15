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

    @RequestMapping(value = "logout",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        ServerResponse<String> response = iUserService.register(user);

        return response;
    }
    /**
     * 检查filed字段是否已存在
     * @param field
     * @param type 类型username，或者email
     * @return
     */
    @RequestMapping(value = "check_valid",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String field,String type){
        ServerResponse<String> response = iUserService.checkValid(field,type);

        return response;
    }

    @RequestMapping(value = "get_user_info",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccessData(user);
        }else {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
    }
    @RequestMapping(value = "forget_get_question",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.getQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 忘记密码时的修改密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    @RequestMapping(value = "reset_password",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(user, passwordNew, passwordOld);
    }

    @RequestMapping(value = "update_Info",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfo(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        //防止横向越权
        //将传入的user的id赋值为session中的userId
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> serverResponse = iUserService.updateInfo(user);
        //判断是否更新成功
        if (serverResponse.isSuccess()) {
            //更新成功的话，将session中的CURRENT_USER也更新
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value = "get_Info",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
        }
        return iUserService.getInfo(currentUser.getId());
    }
}
