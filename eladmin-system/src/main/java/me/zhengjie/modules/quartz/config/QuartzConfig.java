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
package me.zhengjie.modules.quartz.config;

import cn.hutool.core.util.ObjectUtil;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * 定时任务配置
 * @author /
 * @date 2019-01-07
 */
@Configuration
public class QuartzConfig {
	@Autowired
	private Environment environment;
	private final String DB_HOST ="DB_HOST";
	private final String DB_PORT ="DB_PORT";
	private final String DB_NAME ="DB_NAME";

	/**
	 * 解决Job中注入Spring Bean为null的问题
	 */
	@Component("quartzJobFactory")
	public static class QuartzJobFactory extends AdaptableJobFactory {

		private final AutowireCapableBeanFactory capableBeanFactory;

		public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
			this.capableBeanFactory = capableBeanFactory;
		}

		@Override
		protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

			//调用父类的方法
			Object jobInstance = super.createJobInstance(bundle);
			capableBeanFactory.autowireBean(jobInstance);
			return jobInstance;
		}
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(QuartzJobFactory quartzJobFactory) throws IOException {
		//获取配置属性
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("config/quartz.properties"));
		//在quartz.properties中的属性被读取并注入后再初始化对象
		propertiesFactoryBean.afterPropertiesSet();
		// DB_HOST DB_PORT DB_NAME
		Properties properties = propertiesFactoryBean.getObject();
		String url =properties.getProperty("org.quartz.dataSource.qzDS.URL");
		String dbPwd =properties.getProperty("org.quartz.dataSource.qzDS.password");
		String dbHost = environment.getProperty(DB_HOST);
		String dbPort = environment.getProperty(DB_PORT);
		String dbName = environment.getProperty(DB_NAME);
		String realUrl = this.resolveUrl(url,dbHost,dbPort,dbName);
		properties.setProperty("org.quartz.dataSource.qzDS.URL",realUrl);
		if(ObjectUtil.isNotNull(dbPwd)){
			properties.setProperty("org.quartz.dataSource.qzDS.password",dbPwd);
		}
		//创建SchedulerFactoryBean
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setQuartzProperties(properties);
		factory.setJobFactory(quartzJobFactory);//支持在JOB实例中注入其他的业务对象
		factory.setApplicationContextSchedulerContextKey("applicationContextKey");
		factory.setWaitForJobsToCompleteOnShutdown(true);//这样当spring关闭时，会等待所有已经启动的quartz job结束后spring才能完全shutdown。
		factory.setOverwriteExistingJobs(false);//是否覆盖己存在的Job
		factory.setStartupDelay(10);//QuartzScheduler 延时启动，应用启动完后 QuartzScheduler 再启动
		return factory;
	}

	/**
	 * 注入scheduler到spring
	 * @param quartzJobFactory /
	 * @return Scheduler
	 * @throws Exception /
	 */
	@Bean(name = "scheduler")
	public Scheduler scheduler(QuartzJobFactory quartzJobFactory) throws Exception {
		Scheduler scheduler=schedulerFactoryBean(quartzJobFactory).getScheduler();
		return scheduler;
	}

	private String replaceDBInfo(String url){
		return null;
	}

	private String resolveUrl(String url,String DB_HOST,String DB_PORT,String DB_NAME){
		String[] arr = url.split("\\/");
		String[] hostAndPort = arr[2].split("\\$");
		String dbHost = "$" + hostAndPort[1].substring(0,hostAndPort[1].length()-1);
		String dbPort = "$" + hostAndPort[2];
		String[] db = arr[3].split("\\?");
		String dbName = db[0];
		String host = handleHost(dbHost,DB_HOST);
		url = url.replace(dbHost,host);
		String port = handlePort(dbPort,DB_PORT);
		url = url.replace(dbPort,port);
		String name = handleName(dbName,DB_NAME);
		url = url.replace(dbName,name);
		return url;
	}
	private String handleHost(String dbHost,String envHost){
		if(ObjectUtil.isNull(envHost)){
			String[] hostArr = dbHost.split("\\:");
			String ee = hostArr[1].replace("}","");
			return ee;
		}else{
			return envHost;
		}
	}

	private String handlePort(String dbPort,String envPort){
		if(ObjectUtil.isNull(envPort)){
			String[] hostArr = dbPort.split("\\:");
			return hostArr[1].replace("}","");
		}else{
			return envPort;
		}
	}

	private String handleName(String dbName,String envName){
		if(ObjectUtil.isNull(envName)){
			String[] hostArr = dbName.split("\\:");
			return hostArr[1].replace("}","");
		}else{
			return envName;
		}
	}
}
