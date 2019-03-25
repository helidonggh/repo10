package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



@ControllerAdvice//定义控制器增强类
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTION;

    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    @ExceptionHandler(CustomException.class)//捕获此异常
    @ResponseBody//返回json数据
    public ResponseResult customException(CustomException customException){
        ResultCode resultCode = customException.getResponseResult();
        LOGGER.error("catch exception : {}\r\nexception:"+customException.getMessage());//异常日志
        return new ResponseResult(resultCode);
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public ResponseResult exception(Exception e){
        if(EXCEPTION==null){
            EXCEPTION = builder.build();
        }
        ResultCode resultCode = EXCEPTION.get(e.getClass());
        if(resultCode!=null){
            return new ResponseResult(resultCode);
        }else{
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
    static{
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }
}
