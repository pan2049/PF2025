package tw.jdi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SystemStart {

	@Autowired
	private ScheduleUtils scheduleUtils;
	
	@EventListener(ApplicationReadyEvent.class)
	public void run() {
//		scheduleUtils.deploySchedule(null, null, null, null);
	}
}
