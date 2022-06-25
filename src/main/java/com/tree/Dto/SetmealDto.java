package com.tree.Dto;

import com.tree.Entity.Setmeal;
import com.tree.Entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @Auther: 来两碗米饭
 * @ClassName：SetmealDto
 * @Date: 2022/6/21 17:54
 * @Description TODO:
 */
@Data
public class SetmealDto extends Setmeal {
    private  List<SetmealDish> setmealDishes;

    private String categoryName;
}
