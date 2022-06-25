package com.tree.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.Common.R;
import com.tree.Dto.DishDto;
import com.tree.Dto.SetmealDto;
import com.tree.Entity.Dish;
import com.tree.Entity.Setmeal;
import com.tree.Serivice.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: 来两碗米饭
 * @ClassName：SetmealController
 * @Date: 2022/6/21 17:45
 * @Description TODO:
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * @Description TODO:分页数据
     * @MethodName:page
     * @Param: [page, pageSize, name]
     * @Return: com.tree.Common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.tree.Dto.SetmealDto>>
     * @Date: 2022/6/22 14:58
     **/
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page,int pageSize,String name){
        Page<SetmealDto> SetmealPage = setmealService.getPage(page, pageSize, name);
        return R.success(SetmealPage);
    }

    /**
     * @Description TODO:添加套餐
     * @MethodName:save
     * @Param: [setmealDto]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/22 14:58
     **/
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmeal(setmealDto);

        return R.success("添加成功");
    }


    /**
     * @Description TODO:修改套餐的数据回显
     * @MethodName:findById
     * @Param: [id]
     * @Return: com.tree.Common.R<com.tree.Dto.SetmealDto>
     * @Date: 2022/6/22 14:59
     **/
    @GetMapping("/{id}")
    public R<SetmealDto> findById(@PathVariable long id){
        SetmealDto serviceById = setmealService.findById(id);
        return R.success(serviceById);
    }

    /**
     * @Description TODO:修改套餐
     * @MethodName:update
     * @Param: [setmealDto]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/22 14:59
     **/
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmeal(setmealDto);
        return R.success("更新成功");
    }

    /**
     * @Description TODO:删除套餐
     * @MethodName:deleteByIds
     * @Param: [ids]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/22 15:12
     **/
    @DeleteMapping
    public R<String> deleteByIds(String ids){
        setmealService.deleteByIds(ids);
        return R.success("删除成功");
    };

    /**
     * @Description TODO:套餐状态
     * @MethodName:updateStatus
     * @Param: [type, ids]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/22 15:29
     **/
    @PostMapping("/status/{type}")
    public R<String> updateStatus(@PathVariable Integer type,String ids){
        setmealService.updateStatus(type,ids);
        return R.success("修改成功");
    }


    @GetMapping("/list")
    public R<List<SetmealDto>> findByCategoryId(Setmeal setmeal){
        List<SetmealDto> byCategoryId = setmealService.findByCategoryId(setmeal);
        return R.success(byCategoryId);
    }


}
