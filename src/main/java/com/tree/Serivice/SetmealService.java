package com.tree.Serivice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.Dto.DishDto;
import com.tree.Dto.SetmealDto;
import com.tree.Entity.Dish;
import com.tree.Entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    Page<SetmealDto> getPage(int page,int pageSize,String name);

    void saveSetmeal(SetmealDto setmealDto);

    SetmealDto findById(long id);

    void updateSetmeal(SetmealDto setmealDto);

    //删除套餐
    void deleteByIds(String ids);

    void updateStatus(Integer type, String ids);

    List<SetmealDto> findByCategoryId(Setmeal setmeal);
}
