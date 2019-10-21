package com.example.common;

import lombok.extern.log4j.Log4j;

@Log4j
public abstract class BaseTask implements Runnable {
    
    /**实际任务*/
    public abstract void work() throws CustomException;

    @Override
    public void run() {
        try {
            work();
        } catch (CustomException e) {
            log.error("错误码: " + e.getErrorCode() + "错误信息" + e.getMessage());
        }
    }
}
