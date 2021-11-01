package me.zhengjie.modules.quartz.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@IdClass(QrtzIdClass.class) // 联合主键需要一个类指定哪些字段是联合主键
@Table(name="qrtz_cron_triggers")
public class QrtzCronTriggers implements Serializable {
    @Id
    @Column(name = "SCHED_NAME")
    private String schedName;

    @Id
    @Column(name = "TRIGGER_NAME")
    private String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP")
    private String triggerGroup;

    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;

    @Column(name = "TIME_ZONE_ID")
    private String timeZoneId;
}
