package tw.pan.config;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Schedule]的配置
 * 
 * @author PAN
 */

@Configuration
public class ScheduleConfig {

	@Bean
	Scheduler scheduler() throws SchedulerException {
		StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
		Properties properties = new Properties();
		properties.put("org.quartz.scheduler.instanceName", "PFScheduler");
		properties.put("org.quartz.scheduler.instanceIdleWaitTime", "60000");
		properties.put("org.quartz.threadPool.threadCount", "64");
		stdSchedulerFactory.initialize(properties);
		return stdSchedulerFactory.getScheduler();
	}

	@Bean(name = "schedulePool")
	ScheduledExecutorService getScheduledPool() {
		return Executors.newScheduledThreadPool(16);
	}

}
