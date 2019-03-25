package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;

//定义抛出异常的类
public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
