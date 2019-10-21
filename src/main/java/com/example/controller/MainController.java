package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.CustomException;
import com.example.common.CustomResponse;
import com.example.pojo.vo.MongoStatus;
import com.example.pojo.vo.MqttStatus;
import com.example.service.MongoCheckService;
import com.example.service.MqttCheckService;

@RestController
public class MainController {
    
    @Autowired
    private MongoCheckService mongoCheckService;
    
    @Autowired
    private MqttCheckService mqttCheckService;
    
    @GetMapping("/test")
    public String test() {
        return "test";
    }
    
    @GetMapping("/mongo_status")
    public CustomResponse<MongoStatus> getMongoStatus() throws CustomException {
        return new CustomResponse<MongoStatus>(mongoCheckService.getStatus());
        
    }
    
    @GetMapping("/mqtt_status")
    public CustomResponse<MqttStatus> getMqttStatus() throws CustomException {
        return new CustomResponse<MqttStatus>(mqttCheckService.getStatus());
        
    }
}
