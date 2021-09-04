package me.zhengjie.modules.mq.service;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class MQReceiver {

    @RabbitListener(queues= "queue")
    public void receiveMsg(String message){
        log.info("********receive******");
        log.info(message);
    }

    @RabbitListener(queues = "PLUGIN_DELAY_QUEUE")//监听延时队列
    public void fanoutConsumer(Message message, Channel channel){
        String msg = new String(message.getBody());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("【插件延迟队列】【" + sdf.format(new Date()) + "】收到消息：" + msg);
    }
}
