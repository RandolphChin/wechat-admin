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
package me.zhengjie.modules.quartz.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.zhengjie.modules.quartz.domain.QrtzJob;
import me.zhengjie.modules.quartz.service.QuartzJobServices;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.quartz.repository.QrtzJobRepository;
import me.zhengjie.modules.quartz.service.QrtzJobService;
import me.zhengjie.modules.quartz.service.dto.QrtzJobDto;
import me.zhengjie.modules.quartz.service.dto.QrtzJobQueryCriteria;
import me.zhengjie.modules.quartz.service.mapstruct.QrtzJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author randolph
* @date 2021-07-02
**/
@Service
@RequiredArgsConstructor
public class QrtzJobServiceImpl implements QrtzJobService {

    private final QrtzJobRepository qrtzJobRepository;
    private final QrtzJobMapper qrtzJobMapper;
    @Autowired
    private QuartzJobServices quartzJobServices;

    @Override
    public Map<String,Object> queryAll(QrtzJobQueryCriteria criteria, Pageable pageable){
        Page<QrtzJob> page = qrtzJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(qrtzJobMapper::toDto));
    }

    @Override
    public List<QrtzJobDto> queryAll(QrtzJobQueryCriteria criteria){
        return qrtzJobMapper.toDto(qrtzJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public QrtzJobDto findById(Long id) {
        QrtzJob qrtzJob = qrtzJobRepository.findById(id).orElseGet(QrtzJob::new);
        ValidationUtil.isNull(qrtzJob.getId(),"QrtzJob","id",id);
        return qrtzJobMapper.toDto(qrtzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QrtzJobDto create(QrtzJob resources) {
        resources = qrtzJobRepository.save(resources);
        // 保存至 qrtz 表中
        quartzJobServices.addJob(resources.getJobClass(),resources.getJobName(),resources.getGroupName(),resources.getCronExpression(), new JSONObject(resources.getParam()).toBean(Map.class));
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return qrtzJobMapper.toDto(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(QrtzJob resources) {
        QrtzJob qrtzJob = qrtzJobRepository.findById(resources.getId()).orElseGet(QrtzJob::new);
        ValidationUtil.isNull( qrtzJob.getId(),"QrtzJob","id",resources.getId());
        qrtzJob.copy(resources);
        qrtzJobRepository.save(qrtzJob);
        quartzJobServices.updateJob(resources.getJobName(),resources.getGroupName(),resources.getCronExpression(),new JSONObject(resources.getParam()).toBean(Map.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {

        for (Long id : ids) {
            QrtzJob qrtzJob = qrtzJobRepository.findById(id).orElseGet(QrtzJob::new);
            quartzJobServices.deleteJob(qrtzJob.getJobName(),qrtzJob.getGroupName());
            qrtzJobRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<QrtzJobDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QrtzJobDto qrtzJob : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" jobName",  qrtzJob.getJobName());
            map.put(" groupName",  qrtzJob.getGroupName());
            map.put(" jobClass",  qrtzJob.getJobClass());
            map.put(" cronExpression",  qrtzJob.getCronExpression());
            map.put(" param",  qrtzJob.getParam());
            map.put(" createTime",  qrtzJob.getCreateTime());
            map.put(" description",  qrtzJob.getDescription());
            map.put(" updateTime",  qrtzJob.getUpdateTime());
            map.put(" isPause",  qrtzJob.getIsPause());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void pauseJob(Long id) {
        QrtzJob resources = qrtzJobRepository.findById(id).orElseGet(QrtzJob::new);
        if(resources.getIsPause()){
            quartzJobServices.resumeJob(resources.getJobName(),resources.getGroupName());
            resources.setIsPause(false);
        }else {
            quartzJobServices.pauseJob(resources.getJobName(),resources.getGroupName());
            resources.setIsPause(true);
        }
        qrtzJobRepository.save(resources);
    }

    @Override
    public void runOnce(Long id) {
        QrtzJob resources = qrtzJobRepository.findById(id).orElseGet(QrtzJob::new);
        quartzJobServices.runOnce(resources.getJobName(),resources.getGroupName());
    }
}
