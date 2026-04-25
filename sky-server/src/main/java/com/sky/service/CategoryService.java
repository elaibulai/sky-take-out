package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);
    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    void save(CategoryDTO categoryDTO);
    /**
     * 分类启动与禁用
     * @param id status
     * @return
     */
    void start(Integer status, Long id);
    /**
     * 修改分类
     * @param id status
     * @return
     */
    void update(CategoryDTO categoryDTO);
    /**
     * 删除分类
     * @param id status
     */
    void delete(Long id);
    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
