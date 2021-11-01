package me.zhengjie.modules.quartz.repository;

import me.zhengjie.modules.quartz.domain.QrtzCronTriggers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface QrtzCronTriggersRepository extends JpaRepository<QrtzCronTriggers, Long>, JpaSpecificationExecutor<QrtzCronTriggers> {

    @Query(value = "SELECT a.JOB_NAME jobName, a.JOB_GROUP groupName, a.JOB_CLASS_NAME jobClass,b.DESCRIPTION param, b.PREV_FIRE_TIME  prevFireTime, b.NEXT_FIRE_TIME nextFireTime, b.TRIGGER_STATE triggerState, c.CRON_EXPRESSION cronExpression FROM qrtz_job_details a LEFT JOIN qrtz_triggers b ON a.JOB_NAME = b.JOB_NAME AND b.TRIGGER_GROUP = a.JOB_GROUP LEFT JOIN qrtz_cron_triggers c ON b.TRIGGER_NAME = c.TRIGGER_NAME AND b.TRIGGER_GROUP = c.TRIGGER_GROUP where a.JOB_NAME like CONCAT('%', ?1, '%')",nativeQuery = true,
        countQuery = "SELECT count(1) FROM qrtz_job_details a LEFT JOIN qrtz_triggers b ON a.JOB_NAME = b.JOB_NAME AND b.TRIGGER_GROUP = a.JOB_GROUP LEFT JOIN qrtz_cron_triggers c ON b.TRIGGER_NAME = c.TRIGGER_NAME AND b.TRIGGER_GROUP = c.TRIGGER_GROUP where a.JOB_NAME like CONCAT('%', ?1, '%')")
    Page<Map<String,Object>> findAllQuartz(String jobName, Pageable pageable);
}
