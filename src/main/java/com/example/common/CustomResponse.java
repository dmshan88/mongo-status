package com.example.common;

import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**基础API返回对象*/
@Getter
@Setter
public class CustomResponse<T> {
    
    private Long timestamp = new Date().getTime() / 1000; //时间戳
    
    private Integer code = ErrorCode.OK.getCode(); //错误码
    
    private Integer status = 200; //http状态码

    private String message = ErrorCode.OK.getMessage(); //错误信息
    
    private T data = null; //数据
    
    private Long count = null;
    
    public CustomResponse(T t) {
        setData(t);
        if (data instanceof Collection) {
            this.setCount((long) ((Collection<?>) data).size());
        } else if (data != null) {
            this.setCount((long) 1);
        }
    }
    public CustomResponse(T t, Long count) {
        setData(t);
        setCount(count);
    }

    public void setErrorCode(ErrorCode error) {
        setErrorCode(error, null);
    }
    
    public void setErrorCode(ErrorCode error, String message) {
        
        if (error == null) {
            error = ErrorCode.UNKNOWN;
            message = message + "error 输入为空";
        }
        setCode(error.getCode());
        if (message == null || message.isEmpty()) {
            setMessage(error.getMessage());
        } else {
            setMessage(error.getMessage() + ": " + message);
        }
    }
    
}
