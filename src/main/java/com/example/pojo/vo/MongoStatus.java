package com.example.pojo.vo;

import com.example.common.Constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoStatus {
    
    private boolean connected;

    @Setter(value = AccessLevel.NONE)
    private String database = Constant.DATABASE_POINTCARE;
    
    private String table;
    
    private String machineId;
    
    private long checkDatetime;
    
    private long lastInsertTime;
}
