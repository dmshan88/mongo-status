package com.example.common;

import lombok.Getter;

/**自定义异常*/
@SuppressWarnings("serial")
public class CustomException extends Exception {
    @Getter
    private ErrorCode errorCode = ErrorCode.UNKNOWN;
    
    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
}
