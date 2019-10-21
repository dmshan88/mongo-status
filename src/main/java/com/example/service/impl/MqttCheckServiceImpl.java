package com.example.service.impl;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.common.CustomException;
import com.example.common.ErrorCode;
import com.example.pojo.vo.MqttStatus;
import com.example.service.MqttCheckService;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class MqttCheckServiceImpl implements MqttCheckService {

    @Value("${app.mqtt_enable}")
    Boolean mqttEnable;
    
    @Value("${app.mqtt_broker}")
    private String broker;
    
    @Value("${app.mqtt_client_id}")
    private String clientId;
    
    @Value("${app.mqtt_username}")
    private String username;
    
    @Value("${app.mqtt_password}")
    private String password;
    
    private static boolean start = false;
    
    private static Date connectDate = null;
    
    private MqttAsyncClient mqttClient = null;
    
    @Override
    public void checkConnectStatus() throws CustomException {
        if (!mqttEnable) {
            return;
        }
        if (!start) {
            start();
        } else if (!mqttClient.isConnected()) {
            throw new CustomException(ErrorCode.MQTT_CONNECT_ERROR, "mqtt 未连接");
        }
    }
    
    @Override
    public MqttStatus getStatus() throws CustomException {
        MqttStatus status = new MqttStatus();
        status.setEnableCheck(mqttEnable);
        if (mqttEnable) {
            if (!mqttClient.isConnected()) {
                throw new CustomException(ErrorCode.MQTT_CONNECT_ERROR, "mqtt 未连接");
            }
            status.setConnected(mqttClient.isConnected());
            status.setConnectTimestamp(connectDate.getTime()/1000);
        }
        return status;
    }
    
    private void start() throws CustomException {
        log.info("Mqtt start");
        MqttConnectOptions connectOptions = new MqttConnectOptions(); 
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setUserName(username);
        connectOptions.setPassword(password.toCharArray());
        connectOptions.setCleanSession(false);
        connectOptions.setKeepAliveInterval(30);
        try {
            mqttClient = new MqttAsyncClient(broker, clientId);
            mqttClient.setCallback(new MqttCallbackExtended() {
                
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    log.info("messageArrived! topic: " + topic 
                            + "; message: " + message.toString());
                }
                
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("deliveryComplete " + token.getMessageId());
                }
                
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("connectionLost " + cause.getMessage());
                }
                
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    log.info("connectComplete: " + serverURI  + ", is reconnect:" + reconnect);
                    connectDate = new Date();
                }
            });
            mqttClient.connect(connectOptions, null, new IMqttActionListener() {
                
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    log.info("connect success"); 
                }
                
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    log.error("connect error" + exception.getMessage());
                }
            });
        } catch (MqttException e) {
            throw new CustomException(ErrorCode.MQTT_CONNECT_ERROR, e.getMessage());
        }
        start = true;
    }

}