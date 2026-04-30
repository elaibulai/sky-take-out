package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AtuoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    public Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) values" +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    @AtuoFill(value = OperationType.INSERT)
    void save(Category category);

    /**
     *
     * @param category
     */
    @AtuoFill(value = OperationType.UPDATE)
    void update(Category category);
    @Delete("delete from category where id=#{id}")
    void delete(Long id);
    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
