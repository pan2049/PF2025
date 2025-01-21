package tw.jdi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import tw.jdi.service.AlertCheckService;
import tw.jdi.service.impl.ModbusReadService;
import tw.jdi.service.impl.RecordService;
import tw.jdi.utils.cache.DeviceStateCacheManager;
import tw.jdi.utils.quartz.ScheduleUtils;

@Component
public class SystemStart {

	@Autowired
	private ScheduleUtils scheduleUtils;
	@Autowired
	private DeviceStateCacheManager deviceStateCacheManager;
	@Autowired
	private ModbusReadService modbusReadService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private AlertCheckService alertCheckService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void run() {
		scheduleUtils.deploySchedule("ModbusDeviceRead", "分線程不斷線，細分到slave(站點)", "", modbusReadService);
		scheduleUtils.deploySchedule("ViewDataUpdate", "啟動自定義内存(即時數據)裡的websocket主動更新數據任務", "", deviceStateCacheManager);
		scheduleUtils.deploySchedule("Record", "記錄所有設備之狀態與數值，每1分紀錄一筆", "0 * * * * ?", recordService);
		scheduleUtils.deploySchedule("AlertCheck", "異常檢查審核，每1分鐘檢查一次並記錄或更新紀錄", "0 * * * * ?", alertCheckService);
	}
}
