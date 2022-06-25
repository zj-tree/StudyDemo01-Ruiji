package com.tree.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tree.Common.R;
import com.tree.Entity.User;
import com.tree.Serivice.UserService;
import com.tree.Utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.soap.Addressing;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: 来两碗米饭
 * @ClassName：UserController
 * @Date: 2022/6/22 17:49
 * @Description TODO:
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * @Description TODO:发送验证码
     * @MethodName:sendMsg
     * @Param: [user, request]
     * @Return: com.tree.Common.R<java.lang.String>
     * @Date: 2022/6/22 18:25
     **/
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        // request.getSession().setAttribute(user.getPhone(),phone);
        //存入code验证码
        redisTemplate.opsForValue().set(user.getPhone(),code,5, TimeUnit.MINUTES);
        log.info(code);
        return R.success("获取成功");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,String> map,HttpServletRequest request){
        String phone = map.get("phone");
        String code = map.get("code");
        HttpSession session = request.getSession();
        // String codeAB = (String) session.getAttribute(phone);
        Object codeAB = redisTemplate.opsForValue().get(phone);

        //判断验证码是否错误
        if (!code.equals(codeAB)) return R.error("验证码错误");
        //条件生成器
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getPhone,phone);
        User user = userService.getOne(qw);
        System.out.println(user);
        if (user == null){
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }

        session.setAttribute("user",user.getId());
        redisTemplate.delete(phone);
        log.info(String.valueOf(user.getId()));
        //
        return R.success(user);
    }
}
