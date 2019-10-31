package com.example.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.common.Constant;
import com.example.common.CustomException;
import com.example.common.ErrorCode;
import com.example.pojo.PanelError;
import com.example.pojo.PanelResult;
import com.example.pojo.vo.MongoStatus;
import com.example.service.MongoCheckService;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class MongoCheckServiceImpl implements MongoCheckService {
    
    private static boolean conected = false;
    
    @Autowired 
    private MongoTemplate mongoTemplate;
    
    private PanelResult panelResult = null;
    
    private PanelError panelError = null;
    
    private Boolean isResult;
    
    @Override
    public void checkConnectStatus() throws CustomException {
        try {
            mongoTemplate.collectionExists(Constant.DATABASE_POINTCARE);
            conected = true;
        } catch(DataAccessException e) {
            conected = false;
            throw new CustomException(ErrorCode.MONGO_CONNECT_ERROR);
        }
    }

    @Override
    public void checkDataStatus() throws CustomException {
        Sort idDesc = new Sort(Direction.DESC, PanelResult.FIELD_ID);

        try {
            panelResult = mongoTemplate.findOne(new Query().with(idDesc), PanelResult.class);
            panelError = mongoTemplate.findOne(new Query().with(idDesc), PanelError.class);
        } catch(DataAccessException e) {
            throw new CustomException(ErrorCode.MONGO_DATA_ERROR, e.getMessage());
        }
        if (panelResult == null || panelError == null) {
            throw new CustomException(ErrorCode.MONGO_DATA_ERROR, "无记录");
        }
        isResult = panelResult.getInsterTime().compareTo(panelError.getInsterTime()) > 0;
        long currentTimestamp = new Date().getTime();
        long lastTimestamp = isResult ? panelResult.getInsterTime() * 1000 : panelError.getInsterTime() * 1000;
        int inteval = getInterval();
        if (currentTimestamp - lastTimestamp > inteval * 2) {
            log.error("严重怀疑错误 ,lastTimestamp:" + lastTimestamp + "; interval:" + inteval);
            throw new CustomException(ErrorCode.MONGO_DATA_ERROR, "严重怀疑错误！！");
        } else if (currentTimestamp - lastTimestamp > inteval) {
            log.error("怀疑错误 ,lastTimestamp:" + lastTimestamp + "; interval:" + inteval);
            throw new CustomException(ErrorCode.MONGO_DATA_ERROR, "怀疑错误!");
        }
    }
    
    @Override
    public MongoStatus getStatus() throws CustomException {
        MongoStatus status = new MongoStatus();
        if (!conected) {
            throw new CustomException(ErrorCode.MONGO_CONNECT_ERROR);
        }

        status.setTable(isResult ? Constant.TABLE_PANEL_RESULT : Constant.TABLE_PANEL_ERROR);
        status.setMachineId(isResult ? panelResult.getMachineId() : panelError.getMachineId());
        status.setCheckDatetime(isResult ? panelResult.getCheckDatetime() : panelError.getCheckDatetime());
        status.setLastInsertTime(isResult ? panelResult.getInsterTime() : panelError.getInsterTime());
        status.setConnected(conected);
        return status;
    }
    
    /**根据当前时间获取合理间隔*/
    private int getInterval() {
        Calendar calendar = Calendar.getInstance();
        int interval = 60;
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) { 
          //六日
            interval = 60 * 12;
        } else {//平日
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour >= 8 && hour < 12) {//忙时
                interval = 30; //30分钟
            } else if (hour >= 12 && hour < 18) {//闲时
                interval = 60; //1小时
            } else {
                interval = 60 * 12; //12小时
            }
        }
        return interval * 60 * 1000;
    }

}