package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.Dto.DishDto;
import com.tree.Dto.SetmealDto;
import com.tree.Entity.*;
import com.tree.Mapper.SetmealMapper;
import com.tree.Serivice.CategoryService;
import com.tree.Serivice.SetmealDishService;
import com.tree.Serivice.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: 来两碗米饭
 * @ClassName：SetmealServiceImpl
 * @Date: 2022/6/20 13:51
 * @Description TODO:
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * @Description TODO:分页数据获取
     * @MethodName:getPage
     * @Param: [page, pageSize, name]
     * @Return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.tree.Dto.SetmealDto>
     * @Date: 2022/6/21 18:47
     **/
    @Override
    @Transactional
    public Page<SetmealDto> getPage(int page, int pageSize, String name) {
        Page<Setmeal> SetmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.like(name != null,Setmeal::getName,name);
        Page<Setmeal> pageData = super.page(SetmealPage, qw);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(pageData,setmealDtoPage,"records");
        List<SetmealDto> collect = pageData.getRecords().stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(collect);
        return setmealDtoPage;
    }

    @Override
    @Transactional
    public void saveSetmeal(SetmealDto setmealDto) {
        super.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishService.save(setmealDish);
        }
    }

    @Override
    public SetmealDto findById(long id) {
        Setmeal setmeal = super.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
        qw.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(qw);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDto setmealDto) {
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.eq(Setmeal::getId,setmealDto.getId());
        this.update(setmealDto,qw);


        //清空原套餐菜品
        LambdaQueryWrapper<SetmealDish> sqw = new LambdaQueryWrapper<>();
        sqw.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(sqw);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishService.save(setmealDish);
        }

    }

    @Override
    @Transactional
    public void deleteByIds(String ids) {
        String[] split = ids.split(",");
        super.removeBatchByIds(Arrays.asList(split));

        //清空套餐和对应的菜品
        for (String id : split) {
            LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
            qw.eq(SetmealDish::getSetmealId,id);
            setmealDishService.remove(qw);
        }

    }

    @Override
    public void updateStatus(Integer type, String ids) {
        String[] split = ids.split(",");
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(type);
        for (String id : split) {
            setmeal.setId(Long.valueOf(id));
            LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
            qw.eq(Setmeal::getId,id);
            super.update(setmeal,qw);
        }
    }

    @Override
    public List<SetmealDto> findByCategoryId(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        qw.eq(Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> list = super.list(qw);
        List<SetmealDto> collect = list.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long id = item.getId();
            LambdaQueryWrapper<SetmealDish> dqw = new LambdaQueryWrapper<>();
            dqw.eq(SetmealDish::getSetmealId, id);
            List<SetmealDish> setmealDishes = setmealDishService.list(dqw);
            setmealDto.setSetmealDishes(setmealDishes);
            return setmealDto;
        }).collect(Collectors.toList());
        return collect;
    }


}
