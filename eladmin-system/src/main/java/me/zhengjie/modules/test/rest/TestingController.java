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
package me.zhengjie.modules.test.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.test.domain.Testing;
import me.zhengjie.modules.test.service.TestingService;
import me.zhengjie.modules.test.service.dto.TestingQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author wwe
* @date 2021-07-29
**/
@RestController
@Api(tags = "测试管理")
@RequestMapping("/api/testing")
public class TestingController {
    @Autowired
    private TestingService testingService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('testing:list')")
    public void download(HttpServletResponse response, TestingQueryCriteria criteria) throws IOException {
        testingService.download(testingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询测试")
    @ApiOperation("查询测试")
    @PreAuthorize("@el.check('testing:list')")
    public ResponseEntity<Object> query(TestingQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(testingService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增测试")
    @ApiOperation("新增测试")
    @PreAuthorize("@el.check('testing:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Testing resources){
        return new ResponseEntity<>(testingService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改测试")
    @ApiOperation("修改测试")
    @PreAuthorize("@el.check('testing:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Testing resources){
        testingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除测试")
    @ApiOperation("删除测试")
    @PreAuthorize("@el.check('testing:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        testingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /* 接收客户端推送的消息

        @MessageMapping("/chatRoom.send")
        @SendTo("/topic/chatRoom")
     */
    @RequestMapping("/serverToClient")
    public String sendMsg(@Payload String chatMessages){
        messagingTemplate.convertAndSend("/topic/chatRoom", "您收到了新的系统消息");
        System.out.println("====================================================");
        System.out.println(chatMessages);
        return chatMessages;
    }

    @MessageMapping("/chatRoom.send")
    @SendTo("/topic/chatRoom")
    public void receiveFromClient(@Payload String chatMessages){
        System.out.println("************************");
        System.out.println(chatMessages);
    }
}
