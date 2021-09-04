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
package me.zhengjie.rest;

import me.zhengjie.domain.WechatUser;
import me.zhengjie.service.WechatUserService;
import me.zhengjie.service.dto.WechatUserQueryCriteria;
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
* @author randolpy
* @date 2021-09-02
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "微信登录管理")
@RequestMapping("/api/wechatUser")
public class WechatUserController {

    private final WechatUserService wechatUserService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wechatUser:list')")
    public void download(HttpServletResponse response, WechatUserQueryCriteria criteria) throws IOException {
        wechatUserService.download(wechatUserService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询微信登录")
    // @PreAuthorize("@el.check('wechatUser:list')")
    public ResponseEntity<Object> query(WechatUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(wechatUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增微信登录")
    @PreAuthorize("@el.check('wechatUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody WechatUser resources){
        return new ResponseEntity<>(wechatUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改微信登录")
    @PreAuthorize("@el.check('wechatUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody WechatUser resources){
        wechatUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除微信登录")
    @PreAuthorize("@el.check('wechatUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        wechatUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
