package com.tree.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.Common.BaseContext;
import com.tree.Common.R;
import com.tree.Dto.OrdersDto;
import com.tree.Entity.OrderDetail;
import com.tree.Entity.Orders;
import com.tree.Serivice.OrderDetailService;
import com.tree.Serivice.OrderService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: 来两碗米饭
 * @ClassName：OrderController
 * @Date: 2022/6/23 15:02
 * @Description TODO:
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);

        return R.success("下单成功");
    }

    /**
     * @Description TODO:用户订单查看
     * @MethodName:list
     * @Param: [page, pageSize]
     * @Return: com.tree.Common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.tree.Entity.Orders>>
     * @Date: 2022/6/23 18:01
     **/
    @GetMapping("/userPage")
    public R<Page<Orders>> list(int page,int pageSize){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<>();
        qw.eq(Orders::getUserId,userId);
        Page<Orders> pageOrders = new Page<>(page, pageSize);
        Page<Orders> ordersPage = orderService.page(pageOrders,qw);
        return R.success(ordersPage);
    }

    @GetMapping("/page")
    public R<Page<OrdersDto>> page(int page, int pageSize, Long number,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   Date beginTime,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   Date endTime)
    {
        System.out.println(number);
        System.out.println(beginTime);
        System.out.println(endTime);
        Page<Orders> objectPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<>();
        qw.eq(number!=null,Orders::getNumber,number);
        qw.between(beginTime!=null && endTime != null,Orders::getOrderTime,beginTime,endTime);

        Page<Orders> orderPage = orderService.page(objectPage,qw);
        Page<OrdersDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(orderPage,orderDtoPage,"records");
        List<OrdersDto> collect = orderPage.getRecords().stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            String Inumber = item.getNumber();
            LambdaQueryWrapper<OrderDetail> oqw = new LambdaQueryWrapper<>();
            oqw.eq(OrderDetail::getOrderId, Inumber);
            List<OrderDetail> list = orderDetailService.list(oqw);
            ordersDto.setOrderDetails(list);
            return ordersDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(collect);

        return R.success(orderDtoPage);
    }

}
