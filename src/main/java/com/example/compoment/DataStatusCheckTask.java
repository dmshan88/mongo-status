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
import com.example.service.SmsService;

@Component
public class DataStatusCheckTask extends BaseTask {


    @Value("${app.phone_list}")
    private String phoneList;
    
    @Autowired
    private MongoCheckService mongoCheckService;
    
    @Autowired
    private SmsService smsService;
    
    @Override
    public void work() throws CustomException {
        try {
            mongoCheckService.checkDataStatus();
            
        } catch (CustomException e) {
            String message = null;
            if (e.getErrorCode() == ErrorCode.MONGO_DATA_ERROR 
                || e.getErrorCode() == ErrorCode.MONGO_CONNECT_ERROR) {
                message = e.getMessage();

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
