package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/detail.do/")
public class ProductController {

    @Autowired
    private IProductService iProductService;
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse detail(Integer productId) {
       return iProductService.productDetail(productId);
    }


    public ServerResponse<PageInfo> list(Integer categoryId, String keyword,
                                         @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy) {
         return iProductService.productSearch(categoryId, keyword, pageNum, pageSize, orderBy);


    }







}
