package com.tree.Serivice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.Entity.Orders;
import org.springframework.stereotype.Service;

/**
 * @Auther: 来两碗米饭
 * @ClassName：OrderService
 * @Date: 2022/6/23 14:37
 * @Description TODO:
 */
@Service
public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
