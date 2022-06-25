package com.tree.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.Common.R;
import com.tree.Dto.DishDto;
import com.tree.Entity.Category;
import com.tree.Entity.Dish;
import com.tree.Serivice.CategoryService;
import com.tree.Serivice.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Auther: 来两碗米饭
 * @ClassName：DishController
 * @Date: 2022/6/20 22:55
 * @Description TODO:
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @Description TODO:保存菜品
     * @MethodName:save
     * @Param: [dishDto]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/21 12:25
     **/
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    /**
     * @Description TODO:分页查询
     * @MethodName:page
     * @Param: [page, pageSize, name]
     * @Return: com.tree.Common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.tree.Dto.DishDto>>
     * @Date: 2022/6/21 12:25
     **/
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page,int pageSize,String name){
        //构建分页数据
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.like(name != null,Dish::getName,name);
        //降序
        qw.orderByDesc(Dish::getUpdateTime);
        //查询
        Page<Dish> dishList = dishService.page(dishPage, qw);
        log.info(dishList.toString());

        //创建新的分页数据
        Page<DishDto> dishDtoPage = new Page<>();

        //将原分页数据拷贝到新的分页数据,忽略数据
        BeanUtils.copyProperties(dishList,dishDtoPage,"records");

        List<DishDto> collect = dishList.getRecords().stream().map((item) -> {
            //创建新的dishDto对象
            DishDto dishTmp = new DishDto();
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            BeanUtils.copyProperties(item, dishTmp);
            dishTmp.setCategoryName(category.getName());
            return dishTmp;
        }).collect(Collectors.toList());
        //将查询出分类名称的数据返回
        dishDtoPage.setRecords(collect);

        return R.success(dishDtoPage);
    }

    /**
     * @Description TODO:根据id查询菜品数据
     * @MethodName:getById
     * @Param: [id]
     * @Return: com.tree.Common.R<com.tree.Dto.DishDto>
     * @Date: 2022/6/21 15:20
     **/
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable long id){
        DishDto withFlavor = dishService.findWithFlavor(id);
        return R.success(withFlavor);
    }


    /**
     * @Description TODO:修改菜品信息
     * @MethodName:update
     * @Param: [dishDto]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/21 16:33
     **/
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateDishWithFlavor(dishDto);
        String key = "dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("更新成功!");
    }

    /**
     * @Description TODO:删除菜品and批量删除菜品
     * @MethodName:delete
     * @Param: [ids]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/21 17:01
     **/
    @DeleteMapping
    public R<String> delete(String ids){
        dishService.deleteDishWithFlavor(ids);
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("删除成功");
    }

    /**
     * @Description TODO:菜品状态修改
     * @MethodName:updateStatus
     * @Param: [status, ids]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/21 17:46
     **/
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,String ids){
        dishService.updateStatus(status,ids);
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("状态更新成功");
    }

    
    /**
     * @Description TODO:根据分类进行查询
     * @MethodName:findByCategoryId
     * @Param: [categoryId]
     * @Return: com.tree.Common.R<java.util.List < com.tree.Entity.Dish>>
     * @Date: 2022/6/23 9:56
     **/
    // @GetMapping("/list")
    // public R<List<Dish>> findByCategoryId(long categoryId){
    //     LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
    //     qw.eq(Dish::getCategoryId,categoryId);
    //     List<Dish> list = dishService.list(qw);
    //     return R.success(list);
    // }

    @GetMapping("/list")
    public R<List<DishDto>> findByCategoryId(Dish dish){
        List<DishDto> dishData = null;
        String redisKey = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishData = (List<DishDto>)redisTemplate.opsForValue().get(redisKey);
        if (dishData != null){
            return R.success(dishData);
        }

        dishData = dishService.findByCategoryId(dish);
        redisTemplate.opsForValue().set(redisKey,dishData,60, TimeUnit.MINUTES);
        return R.success(dishData);
    }


}
