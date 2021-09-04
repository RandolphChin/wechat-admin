package me.zhengjie;

import me.zhengjie.modules.leaf.IDGen;
import me.zhengjie.modules.leaf.common.Result;
import me.zhengjie.modules.mq.service.MQSender;
import me.zhengjie.modules.quartz.service.QuartzJobServices;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Testing {
    @Autowired
    private IDGen idGen;

    @Test
    public void idg(){
        for(int i=0;i<10;i++){
            Result result = idGen.get("leaf-segment-test");
            System.out.println(result);
        }
    }

    @Autowired
    private MQSender sender;

    @Test
    public void test(){
        sender.delayTest();
    }
}
