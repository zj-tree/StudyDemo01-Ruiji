package com.tree.Serivice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.Dto.DishDto;
import com.tree.Entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto findWithFlavor(long id);

    void updateDishWithFlavor(DishDto dishDto);

    void deleteDishWithFlavor(String ids);

    void updateStatus(int status,String ids);

    List<DishDto> findByCategoryId(Dish dish);
}
