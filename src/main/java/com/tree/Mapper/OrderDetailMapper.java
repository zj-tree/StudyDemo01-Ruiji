package com.tree.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.Entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
