package me.zhengjie.modules.quartz.domain;

import lombok.Data;

import java.io.Serializable;

/**
 *  联合主键需要用到
 */
@Data
public class QrtzIdClass implements Serializable {
    private String schedName;

    private String triggerName;

    private String triggerGroup;
}
