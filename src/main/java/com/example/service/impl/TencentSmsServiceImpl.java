package com.example.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.common.CustomException;
import com.example.common.ErrorCode;
import com.example.service.SmsService;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class TencentSmsServiceImpl implements SmsService {
    
    
    @Value("${app.sms_inteval}")
    private Integer smsInterval;
    
    @Value("${app.sms_enable}")
    Boolean smsEnable;
    
    @Value("${app.sms_appid}")
    int appid;
    
    @Value("${app.sms_appkey}")
    String appkey;
    
    @Value("${app.sms_template_id}")
    int templateId; 
    // 签名
//    String smsSign = "腾讯云";
    
    private static Date lastSendDate = null;
    
    @Override
    public void sendSms(String[] phoneNumbers, String[] params) throws CustomException {
        if (!needSendMessage()) {
            String str = "";
            for (int i = 0; i < params.length; i++) {
                str += params[i];
            }
            log.info(str);
            return;
        }
        try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            SmsMultiSenderResult result =  msender.sendWithParam("86", phoneNumbers,
              templateId, params, "", "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            lastSendDate = new Date();
          log.info(result.toString());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SMS_ERROR, e.getMessage());
        }
    }
    
    /**是否需要发短信*/
    private boolean needSendMessage() {
        if (!smsEnable) {
            log.info("sms disable");
            return false;
        }
        //判断是否到短信间隔
        if (lastSendDate != null && (new Date().getTime() - lastSendDate.getTime()) / 60000 < smsInterval) {
            log.info("interval: " + smsInterval + " min");
            return false;
        }
        return true;
    }
}