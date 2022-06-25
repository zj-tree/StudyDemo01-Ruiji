package com.tree.Common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;

/**
 * @Auther: 来两碗米饭
 * @ClassName：GlobalExceptionHandler
 * @Date: 2022/6/20 00:04
 * @Description TODO:
 */

@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error(e.toString());
        if (e.toString().contains("Duplicate entry")){
            String[] s = e.toString().split(" ");
            log.info(Arrays.toString(s));
            String msg = s[3]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e){
        log.error(e.toString());
        return R.error(e.getMessage());
    }

}
