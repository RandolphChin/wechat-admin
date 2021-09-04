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
package me.zhengjie.modules.quartz.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author randolph
* @date 2021-07-02
**/
@Entity
@Data
@Table(name="qrtz_job")
public class QrtzJob implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "job_name")
    @ApiModelProperty(value = "jobName")
    private String jobName;

    @Column(name = "group_name")
    @ApiModelProperty(value = "groupName")
    private String groupName;

    @Column(name = "job_class")
    @ApiModelProperty(value = "jobClass")
    private String jobClass;

    @Column(name = "cron_expression")
    @ApiModelProperty(value = "cronExpression")
    private String cronExpression;

    @Column(name = "param")
    @ApiModelProperty(value = "param")
    private String param;

    @Column(name = "create_time")
    @ApiModelProperty(value = "createTime")
    private Timestamp createTime;

    @Column(name = "description")
    @ApiModelProperty(value = "description")
    private String description;

    @Column(name = "update_time")
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "is_pause")
    @ApiModelProperty(value = "isPause")
    private Boolean isPause;

    public void copy(QrtzJob source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}