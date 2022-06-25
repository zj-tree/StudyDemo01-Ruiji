package com.tree.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.org.apache.regexp.internal.RE;
import com.tree.Common.BaseContext;
import com.tree.Common.R;
import com.tree.Entity.ShoppingCart;
import com.tree.Serivice.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 来两碗米饭
 * @ClassName：ShopingCartController
 * @Date: 2022/6/23 10:27
 * @Description TODO:
 */

@RestController
@RequestMapping("shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @Description TODO:添加购物车
     * @MethodName:add
     * @Param: [shoppingCart]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/23 10:47
     **/
    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        qw.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart one = shoppingCartService.getOne(qw);
        if (one !=null){
            Integer number = one.getNumber();
            shoppingCart.setNumber(number + 1);
            shoppingCartService.update(shoppingCart,qw);
            return R.success("增加成功");
        }

        shoppingCartService.save(shoppingCart);
        return R.success("添加成功");
    }

    /**
     * @Description TODO:购物车查看
     * @MethodName:list
     * @Param: []
     * @Return: com.tree.Common.R<java.util.List < com.tree.Entity.ShoppingCart>>
     * @Date: 2022/6/23 10:47
     **/
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        qw.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> list = shoppingCartService.list(qw);
        return R.success(list);
    }


    /**
     * @Description TODO:购物车减少
     * @MethodName:delete
     * @Param: [shoppingCart]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/23 13:58
     **/
    @PostMapping("/sub")
    public R<String> delete(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        qw.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart one = shoppingCartService.getOne(qw);

        if (one.getNumber() <= 1){
            shoppingCartService.remove(qw);
            return R.success("删除成功");
        }
        Integer number = one.getNumber();
        shoppingCart.setNumber(number - 1);
        shoppingCartService.update(shoppingCart,qw);
        return R.success("删除成功");
    };


    @DeleteMapping("/clean")
    public R<String> clean(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(qw);
        return R.success("清空成功");
    }

}
