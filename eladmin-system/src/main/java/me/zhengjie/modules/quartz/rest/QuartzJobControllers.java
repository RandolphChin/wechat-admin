package me.zhengjie.modules.quartz.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.quartz.service.QuartzJobServices;
import me.zhengjie.modules.quartz.service.dto.QrtzJobInfo;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/jobs")
public class QuartzJobControllers {

    @Autowired
    private QuartzJobServices quartzJobServices;

    /**
     * 添加新任务
     * @param configDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Object> addJob(@RequestBody QrtzJobInfo configDTO) {
        Map mp = JSONUtil.parseObj(configDTO.getParam()).toBean(Map.class);
        quartzJobServices.addJob(configDTO.getJobClass(), configDTO.getJobName(), configDTO.getGroupName(), configDTO.getCronExpression(), mp);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 暂停任务
     * @param configDTO
     * @return
     */
    @RequestMapping("/pauseJob")
    public ResponseEntity<Object> pauseJob(@RequestBody QrtzJobInfo configDTO) {
        quartzJobServices.pauseJob(configDTO.getJobName(), configDTO.getGroupName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 恢复任务
     * @param configDTO
     * @return
     */
    @RequestMapping("/resumeJob")
    public ResponseEntity<Object> resumeJob(@RequestBody QrtzJobInfo configDTO) {
        quartzJobServices.resumeJob(configDTO.getJobName(), configDTO.getGroupName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 立即运行一次定时任务
     * @param configDTO
     * @return
     */
    @PostMapping("/runOnce")
    public ResponseEntity<Object> runOnce(@RequestBody QrtzJobInfo configDTO) {
        quartzJobServices.runOnce(configDTO.getJobName(), configDTO.getGroupName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 更新任务
     * @param configDTO
     * @return
     */
    @PutMapping
    public ResponseEntity<Object> updateJob(@RequestBody QrtzJobInfo configDTO) {
        Map mp = JSONUtil.parseObj(configDTO.getParam()).toBean(Map.class);
        quartzJobServices.updateJob(configDTO.getJobName(), configDTO.getGroupName(), configDTO.getCronExpression(), mp);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 删除任务
     * @param configDTO
     * @return
     */
    @DeleteMapping("/deleteJob")
    public ResponseEntity<Object> deleteJob(@RequestBody QrtzJobInfo configDTO) {
        quartzJobServices.deleteJob(configDTO.getJobName(), configDTO.getGroupName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 启动所有任务
     * @return
     */
    @RequestMapping("/startAllJobs")
    public ResponseEntity<Object> startAllJobs() {
        quartzJobServices.startAllJobs();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 暂停所有任务
     * @return
     */
    @RequestMapping("/pauseAllJobs")
    public ResponseEntity<Object> pauseAllJobs() {
        quartzJobServices.pauseAllJobs();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 恢复所有任务
     * @return
     */
    @RequestMapping("/resumeAllJobs")
    public ResponseEntity<Object> resumeAllJobs() {
        quartzJobServices.resumeAllJobs();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 关闭所有任务
     * @return
     */
    @RequestMapping("/shutdownAllJobs")
    public ResponseEntity<Object> shutdownAllJobs() {
        quartzJobServices.shutdownAllJobs();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping
    public ResponseEntity<Object> findAllQuartz(Pageable pageable,String jobName){
        if(ObjectUtil.isNull(jobName)){
            jobName="";
        }
        Page<Map<String,Object>> page = quartzJobServices.getAllJob(pageable,jobName);
        return new ResponseEntity<>(PageUtil.toPage(page),HttpStatus.OK);
    }
}
