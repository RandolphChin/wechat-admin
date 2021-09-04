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
package me.zhengjie.service.impl;

import me.zhengjie.domain.WechatUser;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.WechatUserRepository;
import me.zhengjie.service.WechatUserService;
import me.zhengjie.service.dto.WechatUserDto;
import me.zhengjie.service.dto.WechatUserQueryCriteria;
import me.zhengjie.service.mapstruct.WechatUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author randolpy
* @date 2021-09-02
**/
@Service
@RequiredArgsConstructor
public class WechatUserServiceImpl implements WechatUserService {

    private final WechatUserRepository wechatUserRepository;
    private final WechatUserMapper wechatUserMapper;

    @Override
    public Map<String,Object> queryAll(WechatUserQueryCriteria criteria, Pageable pageable){
        Page<WechatUser> page = wechatUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wechatUserMapper::toDto));
    }

    @Override
    public List<WechatUserDto> queryAll(WechatUserQueryCriteria criteria){
        return wechatUserMapper.toDto(wechatUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WechatUserDto findById(Integer id) {
        WechatUser wechatUser = wechatUserRepository.findById(id).orElseGet(WechatUser::new);
        ValidationUtil.isNull(wechatUser.getId(),"WechatUser","id",id);
        return wechatUserMapper.toDto(wechatUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WechatUserDto create(WechatUser resources) {
        return wechatUserMapper.toDto(wechatUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WechatUser resources) {
        WechatUser wechatUser = wechatUserRepository.findById(resources.getId()).orElseGet(WechatUser::new);
        ValidationUtil.isNull( wechatUser.getId(),"WechatUser","id",resources.getId());
        wechatUser.copy(resources);
        wechatUserRepository.save(wechatUser);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            wechatUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<WechatUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WechatUserDto wechatUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("openid", wechatUser.getOpenid());
            map.put(" password",  wechatUser.getPassword());
            map.put("gender", wechatUser.getGender());
            map.put("create_time", wechatUser.getCreateTime());
            map.put("nickname", wechatUser.getNickname());
            map.put("avatar", wechatUser.getAvatar());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public WechatUserDto findByOpenid(String openid) {
        WechatUser wechatUser = wechatUserRepository.findByOpenid(openid);
        return wechatUserMapper.toDto(wechatUser);
    }
}
