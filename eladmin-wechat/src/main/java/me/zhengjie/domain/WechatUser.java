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
package me.zhengjie.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Date;

/**
* @website https://el-admin.vip
* @description /
* @author randolpy
* @date 2021-09-02
**/
@Entity
@Data
@Table(name="wechat_user")
public class WechatUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "openid",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "openid")
    private String openid;

    @Column(name = "password")
    @ApiModelProperty(value = "password")
    private String password;

    @Column(name = "gender")
    @ApiModelProperty(value = "gender")
    private Integer gender;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "create_time")
    private Timestamp createTime;

    @Column(name = "nickname")
    @ApiModelProperty(value = "nickname")
    private String nickname;

    @Column(name = "avatar")
    @ApiModelProperty(value = "avatar")
    private String avatar;

    //最后登录时间
    @Column(name = "last_login_time")
    private Timestamp lastLoginTime;
    //最后登录Ip
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    public void copy(WechatUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
