package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.Common.BaseContext;
import com.tree.Common.CustomException;
import com.tree.Entity.*;
import com.tree.Mapper.OrderMapper;
import com.tree.Serivice.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Auther: 来两碗米饭
 * @ClassName：OrderServiceImpl
 * @Date: 2022/6/23 14:40
 * @Description TODO:
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    private final AddressBookService addressBookService;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;

    public OrderServiceImpl(AddressBookService addressBookService, ShoppingCartService shoppingCartService, UserService userService, OrderDetailService orderDetailService) {
        this.addressBookService = addressBookService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.orderDetailService = orderDetailService;
    }


    /**
     * @Description TODO:下单
     * @MethodName:submit
     * @Param: [orders]
     * @Return: void
     * @Date: 2022/6/23 17:29
     **/
    @Override
    @Transactional
    public void submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        User user = userService.getById(userId);
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(qw);
        if (shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车为空,不能下单");
        }
        if (addressBook == null){
            throw new CustomException("地址为空");
        }

        long orderId = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger();
        List<OrderDetail> collect = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayMethod(1);
        orders.setPhone(user.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAmount(new BigDecimal(amount.get()));

        super.save(orders);
        orderDetailService.saveBatch(collect);

        //清空购物车
        LambdaQueryWrapper<ShoppingCart> sqw = new LambdaQueryWrapper<>();
        sqw.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(qw);

    }
}
