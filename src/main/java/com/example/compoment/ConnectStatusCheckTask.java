package com.example.compoment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.common.BaseTask;
import com.example.common.CustomException;
import com.example.common.ErrorCode;
import com.example.service.MongoCheckService;
import com.example.service.MqttCheckService;
import com.example.service.SmsService;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class ConnectStatusCheckTask extends BaseTask {


    @Value("${app.phone_list}")
    static private String phoneList;
    
    @Autowired
    private MongoCheckService mongoCheckService;
    
    @Autowired
    private MqttCheckService mqttCheckService;
    
    @Autowired
    private SmsService smsService;
    
    @Override
    public void work() throws CustomException {
        try {
            mongoCheckService.checkConnectStatus();
            mqttCheckService.checkConnectStatus();
            
        } catch (CustomException e) {
            String message = null;
            if (e.getErrorCode() == ErrorCode.MQTT_CONNECT_ERROR 
                || e.getErrorCode() == ErrorCode.MONGO_DATA_ERROR 
                || e.getErrorCode() == ErrorCode.MONGO_CONNECT_ERROR) {
                log.error(e);
                message = e.getErrorCode().getMessage();

                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
                String dateString = sdf.format(new Date());
                String[] param = new String[5];
                param[0] = "服务监控软件";
                param[1] = message;
                param[2] = "..";
                param[3] = "..";
                param[4] = dateString;
                String[] phoneNumbers = phoneList.split(",");
                smsService.sendSms(phoneNumbers, param);
            } else {
                throw e;
            }

        }
    }

}
