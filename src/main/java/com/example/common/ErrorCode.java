package com.example.common;

import lombok.Getter;

/**错误码*/
@Getter 
public enum ErrorCode {
    
    UNKNOWN(-1,"初始化错误"),
    OK(0, "成功"),
    EXCEPTION(1, "未知异常"),
    ERROR(2, "未知错误"),
    MONGO_CONNECT_ERROR(3, "mongodb 连接失败"),
    MONGO_DATA_ERROR(4, "mongodb 数据错误"),
    MQTT_CONNECT_ERROR(5, "mqtt 连接失败"),
    SMS_ERROR(6, "短信错误");
    
    private Integer code;

    private String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    /**根据code返回ErrorCode*/
    static public ErrorCode getByCode(Integer code) {
        ErrorCode[] array = ErrorCode.values();
        for (int i = 0; i < array.length; i++) {
            if (array[i].getCode().equals(code)) {
                return array[i];
            }
        }
        return ERROR;
    }
}
