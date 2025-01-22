package tw.pan.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import tw.pan.entity.enumEntity.AlertStatus;
import tw.pan.service.impl.RecordService;
import tw.pan.service.impl.schedule.AlertCheckService;
import tw.pan.service.impl.schedule.ModbusReadService;
import tw.pan.utils.cache.DeviceStateCacheManager;
import tw.pan.utils.quartz.ScheduleUtils;

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
//		scheduleUtils.deploySchedule("ModbusDeviceRead", "分線程不斷線，細分到slave(站點)", "", modbusReadService);
//		scheduleUtils.deploySchedule("ViewDataUpdate", "啟動自定義内存(即時數據)裡的websocket主動更新數據任務", "", deviceStateCacheManager);
//		scheduleUtils.deploySchedule("Record", "記錄所有設備之狀態與數值，每1分紀錄一筆", "0 * * * * ?", recordService);
//		scheduleUtils.deploySchedule("AlertCheck", "異常檢查審核，每1分鐘檢查一次並記錄或更新紀錄", "0 * * * * ?", alertCheckService);
		
		
	}
}
