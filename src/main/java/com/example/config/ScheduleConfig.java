package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.example.compoment.ConnectStatusCheckTask;
import com.example.compoment.DataStatusCheckTask;

/**定时任务配置*/
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
    
    @Value("${app.connect_check_inteval}")
    private Integer connectCheckInterval;
    
    @Value("${app.data_check_inteval}")
    private Integer dataCheckInterval;
    
    @Autowired
    private ConnectStatusCheckTask connectStatusCheckTask;
    
    @Autowired
    private DataStatusCheckTask dataStatusCheckTask;
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addFixedRateTask(connectStatusCheckTask, connectCheckInterval * 1000);
        taskRegistrar.addFixedRateTask(dataStatusCheckTask, dataCheckInterval * 1000);
    }
}
