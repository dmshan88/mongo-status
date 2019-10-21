package com.example.pojo.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MqttStatus {
    
    private boolean enableCheck;
    
    private boolean connected;
    
    private long connectTimestamp;
}
