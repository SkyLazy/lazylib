package com.mmall.controller.portal;

import com.mmall.common.Constants;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
@Controller
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username ,String password ,HttpSession session){
        ServerResponse<User> response= iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Constants.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Constants.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user,HttpSession session){
        ServerResponse<String> response = iUserService.register(user);
        if(response.isSuccess()){
            session.setAttribute(Constants.CURRENT_USER,user);
        }
        return response;
    }

    @RequestMapping(value = "check_vaild.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkVaild(String str ,String type){

        return iUserService.checkVaild(str,type);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByError("登陆已失效，请重新登陆");
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPass(String email){
        return  iUserService.forgetPass(email);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String email,String question,String answer){
        return iUserService.checkAnswer(email,question,answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String email,String forgetToken ,String password){
        return iUserService.forgetResetPassword(email,forgetToken,password);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> ResetPassword(HttpSession session ,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError("登陆已失效，请重新登陆");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }


}
