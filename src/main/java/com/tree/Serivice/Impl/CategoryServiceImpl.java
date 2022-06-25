package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.Common.CustomException;
import com.tree.Common.R;
import com.tree.Entity.Category;
import com.tree.Entity.Dish;
import com.tree.Entity.Setmeal;
import com.tree.Mapper.CategoryMapper;
import com.tree.Serivice.CategoryService;
import com.tree.Serivice.DishService;
import com.tree.Serivice.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 来两碗米饭
 * @ClassName：CategoryServiceImpl
 * @Date: 2022/6/20 11:16
 * @Description TODO:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(long id) {
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(Dish::getCategoryId,id);
        List<Dish> list = dishService.list(qw);
        if (list.size() > 0){
            // return R.error("删除失败,请删除其包含的菜品");
            throw new CustomException("删除失败,请删除其包含的菜品");
        }
        LambdaQueryWrapper<Setmeal> sqw = new LambdaQueryWrapper<>();
        sqw.eq(Setmeal::getCategoryId,id);
        List<Setmeal> list1 = setmealService.list(sqw);

        if (list1.size() > 0){
            // return R.error("删除失败,请删除其包含的菜品");
            throw new CustomException("删除失败,请删除其包含的菜品");
        }
        super.removeById(id);
    }
}
