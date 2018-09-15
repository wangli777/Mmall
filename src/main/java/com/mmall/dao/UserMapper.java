package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查找数据库中当前username记录的条数
     * @param username
     * @return 数据库中username记录的数量
     */
    int checkUserName(String username);

    int checkEmail(String field);
    /**
     * 登录方法
     * @param username 传入的用户名
     * @param password 传入的密码
     * @return 返回根据用户名和密码从数据库中查找到的数据封装好的用户对象
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 根据username找到其对应的找回密码的问题
     * @param username
     * @return
     */
    String  getQuestionByName(String username);

    int checkAnswer(@Param("username") String username,@Param("question") String question, @Param("answer")String answer);

    int updatePasswordByName(@Param("password") String username, @Param("passwordNew") String passwordNew);

    int checkPassword(@Param("userId") Integer userId, @Param("passwordOld") String passwordOld);

    int checkEmailByUserId(@Param("email")String email,@Param("userId") Integer userId);
}