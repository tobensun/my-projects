package com.example.intercept;

import com.example.util.ResponseResult;
import com.example.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局参数异常处理  单个参数校验时异常处理
     */
    @ExceptionHandler(value=ConstraintViolationException.class)
    @ResponseBody
    public ResponseResult jsonErrorHandler(ConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        return ResultUtils.failure(402, message);
    }

    /**
     * 全局参数异常处理  json形式的参数校验异常处理
     */
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult bindErrorHandler(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultUtils.failure(402, message);
    }

    /**
     * 全局异常处理，反正异常返回统一格式的map
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("服务繁忙",e);
        return ResultUtils.failure("服务繁忙");

    }
}
