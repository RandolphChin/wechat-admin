package me.zhengjie.service;


import me.zhengjie.service.dto.FullUserInfo;

public interface WechatService {
    Object login(String code , FullUserInfo fullUserInfo);
}
