package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import jdk.nashorn.internal.parser.Token;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("该用户名不存在");
        }
        String md5EncodeUtf8 = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5EncodeUtf8);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    public ServerResponse<String> register(User user) {
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if (resultCount > 0) {
//            return ServerResponse.createByErrorMessage("该用户已经存在");
//        }
//            resultCount = userMapper.checkEmail(user.getEmail());
//        if (resultCount > 0) {
//            return ServerResponse.createByErrorMessage("该邮箱已被注册");
//        }
        ServerResponse<String> checkValid
                = checkValid(user.getUsername(), Const.USERNAME);
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        checkValid = checkValid(user.getEmail(),Const.EMAIL);
        if (!checkValid.isSuccess()) {
            return checkValid;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME == type) {
                int countResult = userMapper.checkUsername(str);
                if (countResult == 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL == type) {
                int countResult = userMapper.checkEmail(str);
                if (countResult == 0) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> serverResponse =
                                        this.checkValid(username, Const.USERNAME);
        if (serverResponse.isSuccess()) {
            return serverResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            serverResponse.createBySuccess(question);
        }
        serverResponse.createByErrorMessage("该用户无找回密码问题");
        return null;
    }


    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int countResult = userMapper.checkAnswer(username, question, answer);
        if (countResult > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if (StringUtils.equals(token, forgetToken)) {
            String MD5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int countResult = userMapper.updatePasswordByUsername(username, MD5Password);
            if (countResult > 0) {
                return ServerResponse.createBySuccess("密码重置成功");
            }
        }else {
            return ServerResponse.createByErrorMessage("密码重置失败");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }


    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //
        int countResult = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (countResult == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordOld));
        int update = userMapper.updateByPrimaryKeySelective(user);
        if (update > 0) {
            return ServerResponse.createBySuccess("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {

        int countResult = userMapper.checkEmailByUserid(user.getEmail(), user.getId());
        if (countResult > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        countResult = userMapper.updateByPrimaryKeySelective(updateUser);
        if (countResult > 1) {
            return ServerResponse.createBySuccess("用户信息更新成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("用户信息跟新失败");
        }

    @Override
    public ServerResponse<User> getInformation(Integer userid) {
        User user = userMapper.selectByPrimaryKey(userid);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}






