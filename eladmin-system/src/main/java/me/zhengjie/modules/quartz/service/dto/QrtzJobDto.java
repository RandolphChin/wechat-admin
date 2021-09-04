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
package me.zhengjie.modules.quartz.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author randolph
* @date 2021-07-02
**/
@Data
public class QrtzJobDto implements Serializable {

    private Long id;

    private String jobName;

    private String groupName;

    private String jobClass;

    private String cronExpression;

    private String param;

    private Timestamp createTime;

    private String description;

    private Timestamp updateTime;

    private Boolean isPause;
}