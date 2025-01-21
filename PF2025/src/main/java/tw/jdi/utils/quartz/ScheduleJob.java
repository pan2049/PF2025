package tw.jdi.utils.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import tw.jdi.utils.SharedUtils;

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
