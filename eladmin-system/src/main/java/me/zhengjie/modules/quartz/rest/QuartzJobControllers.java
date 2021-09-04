package me.zhengjie.modules.quartz.rest;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.quartz.service.QuartzJobServices;
import me.zhengjie.modules.quartz.service.dto.QuartzConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/addJob")
    public ResponseEntity<Object> addJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobServices.addJob(configDTO.getJobClass(), configDTO.getJobName(), configDTO.getGroupName(), configDTO.getCronExpression(), configDTO.getParam());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 暂停任务
     * @param configDTO
     * @return
     */
    @RequestMapping("/pauseJob")
    public ResponseEntity<Object> pauseJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobServices.pauseJob(configDTO.getJobName(), configDTO.getGroupName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 恢复任务
     * @param configDTO
     * @return
     */
    @RequestMapping("/resumeJob")
    public ResponseEntity<Object> resumeJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobServices.resumeJob(configDTO.getJobName(), configDTO.getGroupName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 立即运行一次定时任务
     * @param configDTO
     * @return
     */
    @RequestMapping("/runOnce")
    public ResponseEntity<Object> runOnce(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobServices.runOnce(configDTO.getJobName(), configDTO.getGroupName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 更新任务
     * @param configDTO
     * @return
     */
    @RequestMapping("/updateJob")
    public ResponseEntity<Object> updateJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobServices.updateJob(configDTO.getJobName(), configDTO.getGroupName(), configDTO.getCronExpression(), configDTO.getParam());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 删除任务
     * @param configDTO
     * @return
     */
    @RequestMapping("/deleteJob")
    public ResponseEntity<Object> deleteJob(@RequestBody QuartzConfigDTO configDTO) {
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
}
