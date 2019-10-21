package com.example.service;

import com.example.common.CustomException;
import com.example.pojo.vo.MqttStatus;

public interface MqttCheckService {
    
    /**检查mqtt状态*/
    void checkConnectStatus() throws CustomException;
    
    /**查询mqtt状态*/
    MqttStatus getStatus() throws CustomException;
}
