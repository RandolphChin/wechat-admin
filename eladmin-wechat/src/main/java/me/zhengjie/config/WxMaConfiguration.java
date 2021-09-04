package me.zhengjie.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@Configuration
public class WxMaConfiguration {
    private final WxMaProperties properties;

    private static Map<String, WxMaService> maServices = new HashMap<>();
    private static String appid;

    @Autowired
    public WxMaConfiguration(WxMaProperties properties) {
        this.properties = properties;
    }

    public static WxMaService getMaService() {
        WxMaService wxService = maServices.get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return wxService;
    }

    @PostConstruct
    public void init() {
        WxMaProperties.Config configs = this.properties.getConfig();
        if (configs == null) {
            throw new WxRuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }
                WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                //     WxMaDefaultConfigImpl config = new WxMaRedisConfigImpl(new JedisPool());
                // 使用上面的配置时，需要同时引入jedis-lock的依赖，否则会报类无法找到的异常
                config.setAppid(configs.getAppid());
                config.setSecret(configs.getSecret());
                appid = configs.getAppid();
                WxMaService service = new WxMaServiceImpl();
                service.setWxMaConfig(config);
                maServices.put(configs.getAppid(),service);
    }

}
