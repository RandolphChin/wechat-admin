package me.zhengjie.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.config.WxMaConfiguration;
import me.zhengjie.service.WechatService;
import me.zhengjie.service.dto.FullUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WechatServiceImpl implements WechatService {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Object login(String code, FullUserInfo fullUserInfo) {
        final WxMaService wxService = WxMaConfiguration.getMaService();
        String sessionKey = "";
        String openid = "";
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            log.info(session.getSessionKey());
            log.info(session.getOpenid());
            sessionKey= session.getSessionKey();
            openid=session.getOpenid();
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
        }
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, fullUserInfo.getRawData(), fullUserInfo.getSignature())) {
            throw new BadRequestException("wechat user check failed");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, fullUserInfo.getEncryptedData(), fullUserInfo.getIv());

        UserDetails userDetails = userDetailsService.loadUserByUsername(openid);
        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info(userInfo.toString());
        return null;
    }

}
