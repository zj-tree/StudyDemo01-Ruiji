package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.Dto.DishDto;
import com.tree.Entity.Dish;
import com.tree.Entity.DishFlavor;
import com.tree.Mapper.DishMapper;
import com.tree.Serivice.DishFlavorService;
import com.tree.Serivice.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: 来两碗米饭
 * @ClassName：DishServiceImpl
 * @Date: 2022/6/20 13:50
 * @Description TODO:
 */
@Service

public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long id = dishDto.getId(); //菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> list = flavors.stream().map((itme) -> {
            itme.setDishId(id);
            return itme;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(list);
    }

    @Override
    public DishDto findWithFlavor(long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(qw);
        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateDishWithFlavor(DishDto dishDto) {
        Dish dish = new Dish();
        LambdaQueryWrapper<Dish> dishQw = new LambdaQueryWrapper<>();
        BeanUtils.copyProperties(dishDto,dish,"flavors");
        dishQw.eq(Dish::getId,dish.getId());
        this.update(dish,dishQw);

        //删除原存的口味数据
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        // //保存新口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> collect = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(collect);

    }

    @Override
    @Transactional
    public void deleteDishWithFlavor(String ids) {
        String[] split = ids.split(",");
        this.removeBatchByIds(Arrays.asList(split));

        for (String s : split) {
            LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
            qw.eq(DishFlavor::getDishId,s);
            dishFlavorService.remove(qw);
        }
    }

    @Override
    public void updateStatus(int status, String ids) {
        String[] split = ids.split(",");
        for (String id : split) {
            Dish dish = new Dish();
            dish.setStatus(status);
            dish.setId(Long.valueOf(id));
            LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
            qw.eq(Dish::getId,id);
            this.update(dish,qw);
        }
    }

    @Override
    public List<DishDto> findByCategoryId(Dish dish) {
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(Dish::getCategoryId,dish.getCategoryId());
        qw.eq(Dish::getStatus,dish.getStatus());
        List<Dish> list = super.list(qw);
        List<DishDto> collect = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dqw = new LambdaQueryWrapper<>();
            dqw.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dfList = dishFlavorService.list(dqw);
            dishDto.setFlavors(dfList);
            return dishDto;
        }).collect(Collectors.toList());
        return collect;
    }


}
