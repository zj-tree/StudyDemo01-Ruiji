package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.Entity.User;
import com.tree.Mapper.UserMapper;
import com.tree.Serivice.UserService;
import org.springframework.stereotype.Service;

/**
 * @Auther: 来两碗米饭
 * @ClassName：UserServiceImpl
 * @Date: 2022/6/22 17:48
 * @Description TODO:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
