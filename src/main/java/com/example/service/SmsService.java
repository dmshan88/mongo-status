package com.example.service;

import com.example.common.CustomException;

public interface SmsService {
    
    /**发送短信*/
    void sendSms(String[] phoneNumbers, String[] params) throws CustomException;
}
