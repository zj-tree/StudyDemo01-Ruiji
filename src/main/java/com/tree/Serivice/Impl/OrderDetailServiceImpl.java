package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.Entity.OrderDetail;
import com.tree.Mapper.OrderDetailMapper;
import com.tree.Serivice.OrderDetailService;
import com.tree.Serivice.OrderService;
import org.springframework.stereotype.Service;


/**
 * @Auther: 来两碗米饭
 * @ClassName：OrderDetailServiceImpl
 * @Date: 2022/6/23 14:41
 * @Description TODO:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
