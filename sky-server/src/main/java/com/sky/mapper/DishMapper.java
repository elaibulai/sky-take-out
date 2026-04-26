package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AtuoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     *
     * @param dish
     */
    @AtuoFill(value = OperationType.INSERT)
    void save(Dish dish);

    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);
    @Delete("delete from dish where id=#{id}")
    void delete(Long id);
    @AtuoFill(value = OperationType.UPDATE)
    void update(Dish dish);
}
