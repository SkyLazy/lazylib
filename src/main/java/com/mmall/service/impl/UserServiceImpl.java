package com.mmall.service.impl;

import com.mmall.common.Constants;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;
import sun.tools.jstat.Token;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service(value = "iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int returnCode = userMapper.checkUserName(username);
        if (returnCode == 0) {
            return ServerResponse.createByError("系统中不存在该用户");
        }
        password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createByError("登录名或密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }

    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = this.checkVaild(user.getUsername(),Constants.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkVaild(user.getEmail(),Constants.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Constants.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int returnCode3 = userMapper.insert(user);
        if (returnCode3 == 0) {
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkVaild(String str, String type) {
        int returnCode = 0;
        if (!StringUtils.isBlank(type)) {
            if (Constants.USERNAME.equals(type)) {
                returnCode = userMapper.checkUserName(str);
                if (returnCode > 0) {
                    return ServerResponse.createByError("用户名已存在");
                }
            }
            if (Constants.EMAIL.equals(type)) {
                returnCode = userMapper.checkUserEmail(str);
                if (returnCode > 0) {
                    return ServerResponse.createByError("该邮箱已注册");
                }
            }
        }else{
            return ServerResponse.createByError("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验通过");
    }

    public ServerResponse<String> forgetPass(String email){
        ServerResponse<String> vaildResponse = this.checkVaild(email,Constants.EMAIL);
        if(vaildResponse.isSuccess()){
            return ServerResponse.createByError("该邮箱尚未注册");
        }
        //通过邮箱查看自己的登陆问题
        String question = userMapper.selectQbyEmail(email);
        if(StringUtils.isBlank(question)){
            return ServerResponse.createByError("用户尚未设置密保问题");
        }
        return ServerResponse.createBySuccessMessage(question);
    }

    public ServerResponse<String> checkAnswer(String email ,String question ,String answer){
        int responseCode = userMapper.checkAnswer(email,question,answer);
        if(responseCode <= 0){
            return ServerResponse.createByError("回答问题不正确");
        }
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX+userMapper.selectUsernameByEmail(email),forgetToken);
        return  ServerResponse.createBySuccess(forgetToken);
    }

    public ServerResponse<String>forgetResetPassword(String email ,String forgetToken ,String newPassword){

        String user_name = userMapper.selectUsernameByEmail(email);
        String cacheToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX+user_name);//key值中加入用户名,有一定的防越权功能
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByError("重置密码失效");
        }
        ServerResponse response = this.checkVaild(email,Constants.EMAIL);
        if(!response.isSuccess()){
            return ServerResponse.createByError("用户不存在");
        }
        if(StringUtils.equals(forgetToken,cacheToken)){
            return ServerResponse.createByError("重置密码超时");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
        int reponseCode = userMapper.updatePasswordByEmail(email,md5Password);
        if(reponseCode>0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByError("修改密码失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByError("旧密码输入错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByError("密码更新失败");
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Constants.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
