package tw.jdi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import tw.jdi.service.impl.ModbusReadService;
import tw.jdi.utils.cache.DeviceStateCacheManager;

@Component
public class SystemStart {

	@Autowired
	private ScheduleUtils scheduleUtils;
	@Autowired
	private DeviceStateCacheManager deviceStateCacheManager;
	@Autowired
	private ModbusReadService modbusReadService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void run() {
		scheduleUtils.deploySchedule("ModbusDeviceRead", "分線程不斷線，細分到slave(站點)", "", modbusReadService);
		scheduleUtils.deploySchedule("ViewDataUpdate", "啟動自定義内存(即時數據)裡的websocket主動更新數據任務", "", deviceStateCacheManager);
		
	}
}
