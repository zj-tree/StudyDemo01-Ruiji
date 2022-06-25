package com.tree.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tree.Common.R;
import com.tree.Entity.Employee;
import com.tree.Serivice.Impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: 来两碗米饭
 * @ClassName：EmployeeController
 * @Date: 2022/6/19 17:34
 * @Description TODO:
 */
// @ResponseBody
// @Controller
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    /**
     * @Description TODO:员工账号登录
     * @MethodName:login
     * @Param: [request, employee]
     * @Return: com.tree.Controller.result.R<com.tree.Entity.Employee>
     * @Date: 2022/6/19 18:21
     **/
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());
        String username = employee.getUsername();
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>();
        qw.eq(Employee::getUsername,username);
        Employee employeeServiceOne = employeeService.getOne(qw);

        if (employeeServiceOne == null){
            return R.error("账号不存在");
        }

        if (!password.equals(employeeServiceOne.getPassword())){
            return R.error("密码错误");
        }

        if (employeeServiceOne.getStatus() == 0){
            return R.error("账号已禁用");
        }



        request.getSession().setAttribute("employee",employeeServiceOne.getId());
        return R.success(employeeServiceOne);
    }

    /**
     * @Description TODO:退出登录
     * @MethodName:logout
     * @Param: [request]
     * @Return: com.tree.Controller.result.R<java.lang.String>
     * @Date: 2022/6/19 20:15
     **/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * @Description TODO:列表显示
     * @MethodName:page
     * @Param: [page, pageSize, name]
     * @Return: com.tree.Controller.result.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.tree.Entity.Employee>>
     * @Date: 2022/6/19 23:47
     **/
    @GetMapping("/page")
    public R<Page<Employee>> page(int page,int pageSize,String name){
        Page<Employee> employeePage = new Page<>(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        //以更新时间降序
        lqw.orderByDesc(Employee::getUpdateTime);
        //判断
        lqw.like(name != null,Employee::getName,name);


        Page<Employee> pageList = employeeService.page(employeePage,lqw);
        return R.success(pageList);
    }


    /**
     * @Description TODO:添加员工
     * @MethodName:save
     * @Param: [employee]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/20 0:50
     **/
    @PostMapping
    public R<String> save(@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("保存成功");
    }

    /**
     * @Description TODO:账号状态and更新用户
     * @MethodName:update
     * @Param: [employee]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/20 10:41
     **/
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.eq(Employee::getId,employee.getId());
        employeeService.update(employee, qw);
        return R.success("更新成功");
    }

    @GetMapping("/{id}")
    public R<Employee> findById(@PathVariable long id){
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.eq(Employee::getId,id);
        Employee employee = employeeService.getOne(qw);
        return R.success(employee);
    }

}
