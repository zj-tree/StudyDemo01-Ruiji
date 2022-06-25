package com.tree.Common;

/**
 * @Auther: 来两碗米饭
 * @ClassName：CustomException
 * @Date: 2022/6/19 23:58
 * @Description TODO:自定义业务异常类
 */
public class CustomException extends RuntimeException{

    public CustomException(String msg){
        super(msg);
    }
}
