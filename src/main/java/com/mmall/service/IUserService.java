package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);

    public ServerResponse<String> register(User user);

    public ServerResponse<String> checkVaild(String str, String type);

    public ServerResponse<String> forgetPass(String email);

    public ServerResponse<String> checkAnswer(String email ,String question ,String answer);

    public ServerResponse<String>forgetResetPassword(String email ,String forgetToken ,String newPassword);

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

}
