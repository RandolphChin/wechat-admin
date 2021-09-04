/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.security.rest;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.zhengjie.annotation.rest.AnonymousDeleteMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.config.WxMaConfiguration;
import me.zhengjie.domain.WechatUser;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.security.config.bean.LoginProperties;
import me.zhengjie.security.config.bean.SecurityProperties;
import me.zhengjie.security.security.TokenProvider;
import me.zhengjie.security.service.OnlineUserService;
import me.zhengjie.security.service.dto.JwtWechatUserDto;
import me.zhengjie.service.WechatUserService;
import me.zhengjie.service.dto.FullUserInfo;
import me.zhengjie.service.dto.WechatUserDto;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "微信接口")
public class WechatAuthorizationController {
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    @Resource
    private LoginProperties loginProperties;
    private final UserDetailsService userDetailsService;
    private final WechatUserService wechatUserService;

    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/wechatLogin")
    public ResponseEntity<Object> login(@RequestBody FullUserInfo fullUserInfo, HttpServletRequest request) throws Exception {
        final WxMaService wxService = WxMaConfiguration.getMaService();
        String sessionKey = "";
        String openid = "";
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(fullUserInfo.getCode());
            log.info(session.getSessionKey());
            log.info(session.getOpenid());
            sessionKey= session.getSessionKey();
            openid=session.getOpenid();
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, fullUserInfo.getRawData(), fullUserInfo.getSignature())) {
            throw new BadRequestException("wechat user check failed");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, fullUserInfo.getEncryptedData(), fullUserInfo.getIv());
        WechatUserDto dto = wechatUserService.findByOpenid(openid);
        WechatUser wechatUser = new WechatUser();
        if(ObjectUtil.isNull(dto)){
            // add wechat user
            wechatUser.setOpenid(openid);
            wechatUser.setAvatar(userInfo.getAvatarUrl());
            wechatUser.setGender(Integer.valueOf(userInfo.getGender()));
            wechatUser.setNickname(userInfo.getNickName());
            wechatUser.setPassword(openid);
        }else{
            BeanUtils.copyProperties(dto,wechatUser);
        }
        wechatUser.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        wechatUser.setLastLoginIp(StringUtils.getIp(request));
        wechatUserService.create(wechatUser);

        // 生成令牌与第三方系统获取令牌方式
        UserDetails userDetails = userDetailsService.loadUserByUsername(openid);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        final JwtWechatUserDto jwtUserDto = (JwtWechatUserDto) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(openid, token);
        }
        return ResponseEntity.ok(authInfo);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        return ResponseEntity.ok(SecurityUtils.getCurrentUser());
    }



    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
