package com.example.service;

import com.example.common.CustomException;
import com.example.pojo.vo.MongoStatus;

public interface MongoCheckService {
    
    /**查询数据库状态*/
    void checkConnectStatus() throws CustomException;
    
    /**查询数据库状态*/
    void checkDataStatus() throws CustomException;

    /**获取mongodb状态*/
    MongoStatus getStatus() throws CustomException;
}
