package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;

//自定义异常类
public class CustomException extends RuntimeException {
    private ResultCode resultCode;
    public CustomException(ResultCode resultCode){
        this.resultCode=resultCode;
    }
    public ResultCode getResponseResult(){
        return this.resultCode;
    }
}
