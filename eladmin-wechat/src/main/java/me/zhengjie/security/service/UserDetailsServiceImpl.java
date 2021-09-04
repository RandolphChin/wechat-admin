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
package me.zhengjie.security.service;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.security.config.bean.LoginProperties;
import me.zhengjie.security.service.dto.JwtWechatUserDto;
import me.zhengjie.service.WechatUserService;
import me.zhengjie.service.dto.WechatUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final WechatUserService wechatUserService;
    private final LoginProperties loginProperties;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    /**
     * 用户信息缓存
     *
     * @see {@link UserCacheClean}
     */
    static Map<String, JwtWechatUserDto> wechatUserDtoCache = new ConcurrentHashMap<>();

    @Override
    public JwtWechatUserDto loadUserByUsername(String username) {
        boolean searchDb = true;
        JwtWechatUserDto jwtUserDto = null;
        if (loginProperties.isCacheEnable() && wechatUserDtoCache.containsKey(username)) {
            jwtUserDto = wechatUserDtoCache.get(username);
            searchDb = false;
        }
        if (searchDb) {
            WechatUserDto user;
            try {
                user = wechatUserService.findByOpenid(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException("", e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("");
            } else {
                jwtUserDto = new JwtWechatUserDto(
                        user
                );
                wechatUserDtoCache.put(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
