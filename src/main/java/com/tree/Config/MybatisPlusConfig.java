package com.tree.Config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: 来两碗米饭
 * @ClassName：MybatisPlusConfig
 * @Date: 2022/6/19 20:22
 * @Description TODO:
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * @Description TODO:mybatisPlus分页插件
     * @MethodName:mybatisPlusInterceptor
     * @Param: []
     * @Return: com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @Date: 2022/6/19 20:25
     **/
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }

}
