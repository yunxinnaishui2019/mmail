package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("用户已存在");
        }
        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("emall已被注册");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        System.out.println(str.toString() + type.toString());
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int countResult = userMapper.checkUsername(str);
                if (countResult > 0) {
                    return ServerResponse.createByErrorMessage("该用户已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int countResult = userMapper.checkEmail(str);
                if (countResult > 0) {
                    return ServerResponse.createByErrorMessage("该email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (!StringUtils.isNotBlank(question)) {
            return ServerResponse.createByErrorMessage("未设置找回密码");
        }
        return ServerResponse.createBySuccessMessage(question);
    }


    public ServerResponse<String> selectAnswer(String username, String question, String answer) {

        ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        int countResult = userMapper.checkAnswer(username, question, answer);
        if (countResult > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    public ServerResponse<String> resetPassword(String username, String passwordNew, String forgetToken) {
        if (!StringUtils.isNotBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("token参数错误");
        }
        ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        if (StringUtils.equals(token, forgetToken)) {
            String password = MD5Util.MD5EncodeUtf8(passwordNew);
            int countResult = userMapper.updatePasswordByUsername(username, password);
            if (countResult > 0) {
                return ServerResponse.createBySuccess("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPass(User user, String passwordOld, String passwordNew) {
        int countResult = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (countResult == 0) {
            return ServerResponse.createBySuccess("旧密码错误");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
        user.setPassword(md5Password);
        countResult = userMapper.updateByPrimaryKeySelective(user);
        if (countResult > 0) {
            return ServerResponse.createBySuccess("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    public ServerResponse<User> updateInformation(User user) {
        int countResult = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (countResult > 0) {
            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int countResult01 = userMapper.updateByPrimaryKeySelective(updateUser);
        if (countResult01 > 0) {
            return ServerResponse.createBySuccess("更新成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse<String> checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();

    }



}
