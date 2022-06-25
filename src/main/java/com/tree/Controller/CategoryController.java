package com.tree.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.Common.R;
import com.tree.Entity.Category;
import com.tree.Entity.Dish;
import com.tree.Entity.Employee;
import com.tree.Entity.Setmeal;
import com.tree.Serivice.CategoryService;
import com.tree.Serivice.DishService;
import com.tree.Serivice.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: 来两碗米饭
 * @ClassName：CategoryController
 * @Date: 2022/6/20 11:05
 * @Description TODO:
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    // private
    private CategoryService categoryService;


    /**
     * @Description TODO:分类管理分页显示
     * @MethodName:page
     * @Param: [page, pageSize]
     * @Return: com.tree.Common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.tree.Entity.Category>>
     * @Date: 2022/6/20 11:22
     **/
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize){
        log.info(page+":"+pageSize);
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Category::getCreateTime);

        Page<Category> categoryPage = categoryService.page(pageInfo,qw);
        return R.success(categoryPage);
    }


    @PostMapping
    public R<String> save(@RequestBody Category category){
        boolean save = categoryService.save(category);
        return save?R.success("保存成功"):R.error("保存失败");
    }

    @DeleteMapping()
    public R<String> deleteById(long id){
        categoryService.remove(id);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.eq(Category::getId,category.getId());
        boolean update = categoryService.update(category, qw);
        return update?R.success("修改成功"):R.error("修改失败");
    }

    @GetMapping("/list")
    public R<List<Category>> findByType(Category category){
        // String key = "category_"+category.get
        System.out.println(category);

        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.eq(category.getType() != null,Category::getType,category.getType());
        List<Category> list = categoryService.list(qw);
        return R.success(list);
    }


}
