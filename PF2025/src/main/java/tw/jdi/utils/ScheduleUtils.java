package tw.jdi.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.jdi.service.ScheduleMission;

/**
 * [排程]的工具
 * 
 * @author PAN
 */

@Service
public class ScheduleUtils {

	@Autowired
	private Scheduler scheduler;

	public class ScheduleJob implements Job {

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			JobDataMap jobDataMap = context.getMergedJobDataMap();
			ScheduleMission scheduleMission = (ScheduleMission) jobDataMap.get("scheduleMission");
			try {
				scheduleMission.doJob();
			} catch (Exception e) {
				SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 工具 -> [部署]排程
	 * 
	 * @param String      jobKeyStr
	 * @param String      info
	 * @param String      cronStr
	 * @param ScheduleRun scheduleRun
	 * @author PAN
	 * @author KYUU
	 */
	public void deploySchedule(String jobKeyStr, String info, String cronStr, ScheduleMission scheduleMission) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("jobKeyStr", jobKeyStr);
		jobDataMap.put("scheduleMission", scheduleMission);
		JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).setJobData(jobDataMap)
				.withIdentity(JobKey.jobKey(jobKeyStr)).build();
		Trigger trigger;
		if (cronStr.equals("")) {
			trigger = TriggerBuilder.newTrigger().build();
		} else {
			trigger = TriggerBuilder.newTrigger()
					.withSchedule(
							CronScheduleBuilder.cronSchedule(cronStr).withMisfireHandlingInstructionFireAndProceed())
					.build();
		}
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.start();
			SharedUtils.getLogger(this.getClass()).info("[DEPLOY_SCHEDULE] " + info + " -> START");
		} catch (SchedulerException e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage() + " -> " + jobKeyStr);
		}
	}

	/**
	 * 工具 -> [移除]排程
	 * 
	 * @param String jobKeyStr
	 * @param String info
	 * @author PAN
	 * @author KYUU
	 */
	public void removeSchedule(String jobKeyStr, String info) {
		try {
			scheduler.deleteJob(JobKey.jobKey(jobKeyStr));
			SharedUtils.getLogger(this.getClass()).info("[REMOVE_SCHEDULE] " + info + " -> CLEAR");
		} catch (SchedulerException e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage() + " -> " + jobKeyStr);
		}
	}

	/**
	 * 工具 -> [檢查]排程是否存在
	 * 
	 * @param String jobKeyStr
	 * @return boolean
	 * @author PAN
	 * @author KYUU
	 */
	public boolean checkSchedule(String jobKeyStr) {
		try {
			return scheduler.checkExists(JobKey.jobKey(jobKeyStr));
		} catch (SchedulerException e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage() + " -> " + jobKeyStr);
			return false;
		}
	}

}
