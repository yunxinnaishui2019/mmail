package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse<String> addCategory(@RequestParam(value = "parentId",
            defaultValue = "0") String categoryName,Integer parentId,HttpSession session) {

        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName,parentId);
        } else {
            return ServerResponse.createByErrorMessage("你不是管理员，无法添加商品");
        }

    }
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session, String categoryName, Integer categoryId) {
        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.setCategoryName(categoryName,categoryId);
        } else {
            return ServerResponse.createByErrorMessage("你不是管理员，无法添加商品");
        }
    }

    @RequestMapping("get_category.do")
    public ServerResponse getChildrenParallelCategory(
                                //把请求中指定名称的参数给控制器中的形参赋值
            HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("你不是管理员");

    }
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getDeepCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByErrorMessage("你不是管理员");
    }





}
