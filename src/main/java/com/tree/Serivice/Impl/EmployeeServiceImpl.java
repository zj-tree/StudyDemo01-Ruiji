package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.Entity.Employee;
import com.tree.Mapper.EmployeeMapper;
import com.tree.Serivice.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Auther: 来两碗米饭
 * @ClassName：EmployeeServiceImpl
 * @Date: 2022/6/19 17:15
 * @Description TODO:公司员工业务层
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
