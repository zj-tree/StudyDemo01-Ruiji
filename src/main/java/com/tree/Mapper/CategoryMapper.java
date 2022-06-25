package com.tree.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.Entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Auther: 来两碗米饭
 * @ClassName：CategoryMapper
 * @Date: 2022/6/20 11:14
 * @Description TODO:
 */
@Repository
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
