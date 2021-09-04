package me.zhengjie.modules.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

    @Bean
    public Queue queue() {
        return new Queue("queue",true,false,false);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExcance",true,false);
    }

    @Bean
    public Binding fanoutBind(){
        return BindingBuilder.bind(queue()).to(fanoutExchange());
    }

    @Bean
    public CustomExchange pluginDelayExchange(){
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("x-delayed-type", "direct");//必须要配置这个类型，可以是direct,topic和fanout
        //第二个参数必须为x-delayed-message
        return new CustomExchange("PLUGIN_DELAY_EXCHANGE","x-delayed-message",false, false, argMap);
    }

    @Bean("pluginDelayQueue")
    public Queue pluginDelayQueue(){
        return new Queue("PLUGIN_DELAY_QUEUE");
    }

    @Bean
    public Binding pluginDelayBinding(@Qualifier("pluginDelayQueue") Queue queue, @Qualifier("pluginDelayExchange") CustomExchange customExchange){
        return BindingBuilder.bind(queue).to(customExchange).with("delay").noargs();
    }
}
