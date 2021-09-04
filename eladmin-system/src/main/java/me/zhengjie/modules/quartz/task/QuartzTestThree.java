package me.zhengjie.modules.quartz.task;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@DisallowConcurrentExecution
public class QuartzTestThree implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(" 666 " + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));

    }
}
