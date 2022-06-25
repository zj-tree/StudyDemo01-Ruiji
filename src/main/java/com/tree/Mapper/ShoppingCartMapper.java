package com.tree.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.Entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Auther: 来两碗米饭
 * @ClassName：ShoppingCartMapper
 * @Date: 2022/6/23 10:35
 * @Description TODO:
 */
@Repository
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
