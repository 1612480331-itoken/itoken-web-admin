package com.example.itokenwebadmin.config.exceptionHandle;

import com.example.itokenwebadmin.config.baseResult.Result;
import com.example.itokenwebadmin.utils.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class ExceptionHandle {


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof UserException) {
            UserException userException = (UserException) e;
            return ResultUtil.error(userException);
        } else {
            return ResultUtil.error(-1, e.getMessage());
        }
    }
}
