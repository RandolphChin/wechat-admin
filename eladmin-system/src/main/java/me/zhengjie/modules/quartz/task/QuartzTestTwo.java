package me.zhengjie.modules.quartz.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuartzTestTwo implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.out.println(jobExecutionContext.getScheduler().getSchedulerInstanceId() + "--TestTwo " + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
        } catch (SchedulerException e) {
            System.out.println("任务执行失败");
        }
    }
}
