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
package me.zhengjie.modules.test.service.impl;

import me.zhengjie.modules.test.domain.Testing;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.test.repository.TestingRepository;
import me.zhengjie.modules.test.service.TestingService;
import me.zhengjie.modules.test.service.dto.TestingDto;
import me.zhengjie.modules.test.service.dto.TestingQueryCriteria;
import me.zhengjie.modules.test.service.mapstruct.TestingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
* @author wwe
* @date 2021-07-29
**/
@Service
@RequiredArgsConstructor
public class TestingServiceImpl implements TestingService {

    private final TestingRepository testingRepository;
    private final TestingMapper testingMapper;

    @Override
    public Map<String,Object> queryAll(TestingQueryCriteria criteria, Pageable pageable){
        Page<Testing> page = testingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(testingMapper::toDto));
    }

    @Override
    public List<TestingDto> queryAll(TestingQueryCriteria criteria){
        return testingMapper.toDto(testingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TestingDto findById(Long id) {
        Testing testing = testingRepository.findById(id).orElseGet(Testing::new);
        ValidationUtil.isNull(testing.getId(),"Testing","id",id);
        return testingMapper.toDto(testing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestingDto create(Testing resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        return testingMapper.toDto(testingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Testing resources) {
        Testing testing = testingRepository.findById(resources.getId()).orElseGet(Testing::new);
        ValidationUtil.isNull( testing.getId(),"Testing","id",resources.getId());
        testing.copy(resources);
        testingRepository.save(testing);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            testingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TestingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TestingDto testing : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("展区", testing.getDeptId());
            map.put("场馆", testing.getJobId());
            map.put("姓名", testing.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}