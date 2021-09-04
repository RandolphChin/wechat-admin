package me.zhengjie;

import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(hidden = true)
@SpringBootApplication
@EnableTransactionManagement
public class WechatRun {

    public static void main(String[] args) {
        SpringApplication.run(WechatRun.class,args);
    }
}
