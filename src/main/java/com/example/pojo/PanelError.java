package com.example.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.common.Constant;

import lombok.Value;

@Value
@Document(collection = Constant.TABLE_PANEL_ERROR)
public class PanelError {
    
    public static final String FIELD_ID = "id";
    public static final String FIELD_CHECK_DATETIME = "chkdatetime";
    public static final String FIELE_ERROR_CODE = "errcode";
    public static final String FIELD_MACHINE_ID = "machineid";
    public static final String FIELD_PANEL_ID = "panelid";
    public static final String FIELD_PANEL_INDEX = "panelindex";
    public static final String FIELD_PANEL_LOT = "panellot";
    
    @Id
    private String id;
    
    @Field(FIELD_CHECK_DATETIME)
    private long checkDatetime;
    
    @Field(FIELE_ERROR_CODE)
    private int errorCode;
    
    @Field(FIELD_MACHINE_ID)
    private String machineId;
    
    @Field(FIELD_PANEL_ID)
    private int panelId;
    
    @Field(FIELD_PANEL_INDEX)
    private int panelIndex;
    
    @Field(FIELD_PANEL_LOT)
    private String panelLot;
    
    public Long getInsterTime() {
        return Long.parseLong(id.substring(0, 8), 16);
    }
}
