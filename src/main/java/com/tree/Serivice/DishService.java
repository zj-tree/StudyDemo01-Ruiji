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

    /**
     * @Description TODO:根据分类查询菜品
     * @MethodName:findByCategoryId
     * @Param: [dish]
     * @Return: java.util.List<com.tree.Dto.DishDto>
     * @Date: 2022/6/25 14:10
     **/
    List<DishDto> findByCategoryId(Dish dish);
}
