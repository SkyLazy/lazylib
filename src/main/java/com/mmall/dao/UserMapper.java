package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized;

import javax.swing.*;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String username);

    int checkUserEmail(String email);

    User selectLogin(@Param("username") String username ,@Param("password") String password);

    String selectQbyEmail(String email);

    int checkAnswer(@Param("email")String email,@Param("question") String question,@Param("password") String password);

    String selectUsernameByEmail(String email);

    int updatePasswordByEmail(@Param("email") String email,@Param("password") String password);

    int checkPassword(@Param("password") String password , @Param("id") int id);
}