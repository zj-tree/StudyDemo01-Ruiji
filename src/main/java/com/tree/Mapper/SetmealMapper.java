package com.tree.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SetmealMapper extends BaseMapper<com.tree.Entity.Setmeal> {
}
