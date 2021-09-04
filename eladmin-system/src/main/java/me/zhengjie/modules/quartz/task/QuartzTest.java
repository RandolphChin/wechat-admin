package me.zhengjie.modules.quartz.task;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

// 如果实例不允许并发执行，一定要加这个标签
@DisallowConcurrentExecution
public class QuartzTest implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.out.println(jobExecutionContext.getScheduler().getSchedulerInstanceId() + "-- Test" + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
        } catch (SchedulerException e) {
            System.out.println("任务执行失败");
        }
    }
}
