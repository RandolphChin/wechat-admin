package me.zhengjie.modules.mq.rest;

import me.zhengjie.modules.mq.service.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private MQSender sender;


    @RequestMapping("/api/test")
    public String test(){
        sender.delayTest();
        // sender.generateDynamicQueue();
        return "ok";
    }
}
