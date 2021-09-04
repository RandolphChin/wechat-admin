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
package me.zhengjie.modules.quartz.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.quartz.domain.QrtzJob;
import me.zhengjie.modules.quartz.service.QrtzJobService;
import me.zhengjie.modules.quartz.service.dto.QrtzJobQueryCriteria;
import me.zhengjie.modules.quartz.service.dto.QuartzConfigDTO;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author randolph
* @date 2021-07-02
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "Quartz集群定时管理")
@RequestMapping("/api/qrtzJob")
public class QrtzJobController {

    private final QrtzJobService qrtzJobService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('qrtzJob:list')")
    public void download(HttpServletResponse response, QrtzJobQueryCriteria criteria) throws IOException {
        qrtzJobService.download(qrtzJobService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Quartz集群定时")
    @ApiOperation("查询Quartz集群定时")
    @PreAuthorize("@el.check('qrtzJob:list')")
    public ResponseEntity<Object> query(QrtzJobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(qrtzJobService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Quartz集群定时")
    @ApiOperation("新增Quartz集群定时")
    @PreAuthorize("@el.check('qrtzJob:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody QrtzJob resources){
        return new ResponseEntity<>(qrtzJobService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Quartz集群定时")
    @ApiOperation("修改Quartz集群定时")
    @PreAuthorize("@el.check('qrtzJob:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody QrtzJob resources){
        qrtzJobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Quartz集群定时")
    @ApiOperation("删除Quartz集群定时")
    @PreAuthorize("@el.check('qrtzJob:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        qrtzJobService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("暂停或恢复Quartz集群定时")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> pauseJob(@PathVariable Long id){
        qrtzJobService.pauseJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 立即运行一次定时任务
     */
    @ApiOperation("删除Quartz集群定时")
    @RequestMapping("/runOnce/{id}")
    public ResponseEntity<Object> runOnce(@PathVariable Long id) {
        qrtzJobService.runOnce(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
