package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str, String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> selectAnswer(String username, String question, String answer);
    ServerResponse<String> resetPassword(String username, String passwordNew, String forgetToken);
    ServerResponse<String> resetPass(User user, String passwordOld, String passwordNew);
    ServerResponse<User> updateInformation(User user);
    ServerResponse<User> getInformation(Integer userId);
    ServerResponse<String> checkAdminRole(User user);
}
