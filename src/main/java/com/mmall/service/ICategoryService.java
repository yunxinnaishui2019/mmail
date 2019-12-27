package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> setCategoryName(String categoryName,Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
