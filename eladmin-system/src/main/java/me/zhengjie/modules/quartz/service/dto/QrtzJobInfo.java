package me.zhengjie.modules.quartz.service.dto;

import lombok.Data;

@Data
public class QrtzJobInfo {
    private Long prevFireTime;
    private Long nextFireTime;
    private String triggerState;
    private String description;
    private String param;
    private String jobName;

    /**
     * 任务所属组
     */
    private String groupName;

    /**
     * 任务执行类
     */
    private String jobClass;

    /**
     * 任务调度时间表达式
     */
    private String cronExpression;
}
