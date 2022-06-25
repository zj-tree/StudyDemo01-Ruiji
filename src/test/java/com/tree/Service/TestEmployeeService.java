package com.tree.Service;

import com.tree.Entity.Employee;
import com.tree.Serivice.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Auther: 来两碗米饭
 * @ClassName：TestEmployeeService
 * @Date: 2022/6/19 17:19
 * @Description TODO:
 */
@SpringBootTest

public class TestEmployeeService {
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testFindAll(){
        List<Employee> list = employeeService.list();
        System.out.println(list);

    }
}
