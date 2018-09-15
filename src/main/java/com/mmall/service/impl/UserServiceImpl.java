package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        //判断密码是否正确
        User user = userMapper.login(username, md5Password);

        if (user==null){
            //密码错误
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //将密码置为空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);

    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> valid = this.checkValid(user.getUsername(), Const.USERNAME);
        //检验用户名是否已存在
        if (!valid.isSuccess()){
            return valid;
        }
        //检验email是否已存在
        valid = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!valid.isSuccess()) {
            return valid;
        }

        //设置用户角色为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int i = userMapper.insertSelective(user);
        if (i > 0) {
            return ServerResponse.createBySuccessMessage("注册成功");
        }else {
            return ServerResponse.createByErrorMessage("注册失败");
        }
    }

    /**
     * 检查field是否在数据库中已经存在
     * @param field 要检查的字段
     * @param type 字段类型，username或者email
     * @return 返回字段是否存在相关信息
     */
    @Override
    public ServerResponse<String> checkValid(String field,String type){
        if (StringUtils.isNotBlank(type)){
            if (Const.USERNAME.equals(type)){
                if (userMapper.checkUserName(field)>0){
                    return ServerResponse.createByErrorMessage("该用户名已存在");
                }

            }
            if (Const.EMAIL.equals(type)){
                if (userMapper.checkEmail(field)>0){
                    return ServerResponse.createByErrorMessage("该邮箱已存在");
                }

            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> getQuestion(String username) {
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.getQuestionByName(username);
        if (StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccessData(question);
        }
        return ServerResponse.createByErrorMessage("您未设置找回密码的问题");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int i = userMapper.checkAnswer(username, question, answer);
        if (i > 0) {
            //说明问题答案验证成功
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccessData(forgetToken);
        }
        return ServerResponse.createByErrorMessage("答案错误");
    }
    @Override
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken) {
        //判断token是否为空
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("token为空，不能修改密码");
        }
        //判断username是否存在
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //判断forgetToken是否正确
        String realToken = TokenCache.get(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(realToken)) {
            return ServerResponse.createByErrorMessage("Token无效或者已过期");
        }
        if (!StringUtils.equals(realToken,forgetToken)){
            //token错误
            return ServerResponse.createByErrorMessage("token错误，不能修改密码");
        }else {
            //token正确，修改密码
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int i = userMapper.updatePasswordByName(username, md5Password);
            if (i>0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }
    @Override
    public ServerResponse<String> resetPassword(User user, String passwordNew, String passwordOld) {
        //一定要先校验这个用户的旧密码，而且必须指定用户id，防止横向越权
        int checkCount = userMapper.checkPassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if (checkCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");

        }
        //设置新密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        //可以更改密码
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }
    @Override
    public ServerResponse<User> updateInfo(User user) {
        //email要进行校验，校验新的email是否已存在
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在，请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInfo(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessData(user);
    }
}
