package tw.pan.service.impl.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.pan.dao.AlertRecordDao;
import tw.pan.dao.AlertSetDao;
import tw.pan.entity.enumEntity.AlertStatus;
import tw.pan.entity.enumEntity.IoType;
import tw.pan.entity.enumEntity.IoType.SignalType;
import tw.pan.entity.po.AlertRecord;
import tw.pan.entity.po.AlertSet;
import tw.pan.entity.po.DeviceState;
import tw.pan.utils.SharedUtils;
import tw.pan.utils.cache.Cache;
import tw.pan.utils.cache.CustomKeyPair;
import tw.pan.utils.cache.DeviceStateCacheManager;
import tw.pan.utils.discord.DiscordService;
import tw.pan.utils.quartz.ScheduleMission;

/**
 * Point alert check service
 * 
 * @author PAN
 */
@Service
public class AlertCheckService extends Cache<CustomKeyPair<SignalType, Integer>, Integer> implements ScheduleMission {

	@Autowired
	private AlertSetDao alertSetDao;
	@Autowired
	private AlertRecordDao alertRecordDao;
	@Autowired
	private DeviceStateCacheManager deviceStateCacheManager;
	@Autowired
	private DiscordService discordService;
	
	@Autowired
	private void initMap() {
		List<AlertRecord> alertRecordListDIO = alertRecordDao.selectLastRecordForInitDIO();
		List<AlertRecord> alertRecordListAIO = alertRecordDao.selectLastRecordForInitAIO();
		if(!alertRecordListDIO.isEmpty()) {
			for(AlertRecord alertRecord : alertRecordListDIO) {
				super.setData(
						new CustomKeyPair<SignalType, Integer>(SignalType.DIGITAL, alertRecord.getPointId()), alertRecord.getAlertId());
			}
		}
		if(!alertRecordListAIO.isEmpty()) {
			for(AlertRecord alertRecord : alertRecordListAIO) {
				super.setData(
						new CustomKeyPair<SignalType, Integer>(SignalType.ANALOG, alertRecord.getPointId()), alertRecord.getAlertId());
			}
		}
	}
	
	@Override
	public void doJob() {
		checkAlertDIO();
		checkAlertAIO();
	}
	
	private void checkAlertDIO() {
		List<AlertSet> alertSetListDIO = alertSetDao.selectAllAlertSetDIO();
		Integer pointId = null;
		IoType ioType = null;
		AlertStatus alertStatus = null;
		Boolean alertDefine = null;
		DeviceState deviceState = null;
		Boolean state = null;
		CustomKeyPair<IoType.ViewType, Integer> cacheKey = null;
		CustomKeyPair<IoType.SignalType, Integer> alertTagKey = null;
		for(AlertSet alertSet : alertSetListDIO) {
			pointId = alertSet.getPointId();
			ioType = alertSet.getIoType();
			alertStatus = alertSet.getAlertStatus();
			alertDefine = alertSet.getAlertDefine().equals(AlertStatus.ON) ? true : false;
			cacheKey = new CustomKeyPair<IoType.ViewType, Integer>(ioType.getViewType(), pointId);
			alertTagKey = new CustomKeyPair<IoType.SignalType, Integer>(ioType.getSignalType(), pointId);
			deviceState = deviceStateCacheManager.getData(cacheKey);
			if(deviceState == null) {
				continue;
			}
			state = deviceState.getState();
			if(state == null) {
				continue;
			}
			switch(alertStatus) {
			case ON -> {
				if(state.equals(alertDefine)) {
					if(!super.hasData(alertTagKey)) {
						Integer alertId = alertRecordDao.insertAlertRecordDIO(pointId, state);
						super.setData(alertTagKey, alertId);
						discordService.sendDIOAlertMessage(alertSet.getPointName(), state, true);
					}
				}else {
					if(super.hasData(alertTagKey)) {
						Integer alertId = super.getData(alertTagKey);
						alertRecordDao.updateAlertRecordDIO(alertId, SharedUtils.getSystemTime());
						super.removeData(alertTagKey);
						discordService.sendDIOAlertMessage(alertSet.getPointName(), state, false);
					}
				}
			}
			case OFF -> {
				if(super.hasData(alertTagKey)) {
					// 手動異常回歸, 更新紀錄並刪除Map資料
					Integer alertId = super.getData(alertTagKey);
					alertRecordDao.updateAlertRecordDIO(alertId, SharedUtils.getSystemTime());
					super.removeData(alertTagKey);
					discordService.sendDIOAlertMessage(alertSet.getPointName(), state, false);
				}
			}
			}
		}
	}
	
	private void checkAlertAIO() {
		List<AlertSet> alertSetListAIO = alertSetDao.selectAllAlertSetAIO();
		Integer pointId = null;
		IoType ioType = null;
		AlertStatus alertStatus = null;
		Float alertUpper = null;
		Float alertLower = null;
		DeviceState deviceState = null;
		Float value = null;
		CustomKeyPair<IoType.ViewType, Integer> cacheKey = null;
		CustomKeyPair<IoType.SignalType, Integer> alertTagKey = null;
		for(AlertSet alertSet : alertSetListAIO) {
			pointId = alertSet.getPointId();
			ioType = alertSet.getIoType();
			alertStatus = alertSet.getAlertStatus();
			alertUpper = alertSet.getAlertUpper();
			alertLower = alertSet.getAlertLower();
			cacheKey = new CustomKeyPair<IoType.ViewType, Integer>(ioType.getViewType(), pointId);
			alertTagKey = new CustomKeyPair<IoType.SignalType, Integer>(ioType.getSignalType(), pointId);
			deviceState = deviceStateCacheManager.getData(cacheKey);
			if(deviceState == null) {
				continue;
			}
			value = deviceState.getValue();
			if(value == null) {
				continue;
			}
			switch(alertStatus) {
			case ON -> {
				if(value > alertUpper || value < alertLower) {
					if(!super.hasData(alertTagKey)) {
						Integer alertId = alertRecordDao.insertAlertRecordAIO(pointId, value);
						super.setData(alertTagKey, alertId);
						discordService.sendAIOAlertMessage(alertSet.getPointName(), value, true);
					}
				}else {
					if(super.hasData(alertTagKey)) {
						Integer alertId = super.getData(alertTagKey);
						alertRecordDao.updateAlertRecordAIO(alertId, SharedUtils.getSystemTime());
						super.removeData(alertTagKey);
						discordService.sendAIOAlertMessage(alertSet.getPointName(), value, false);
					}
				}
			}
			case OFF -> {
				if(super.hasData(alertTagKey)) {
					// 手動異常回歸, 更新紀錄並刪除Map資料
					Integer alertId = super.getData(alertTagKey);
					alertRecordDao.updateAlertRecordAIO(alertId, SharedUtils.getSystemTime());
					super.removeData(alertTagKey);
					discordService.sendAIOAlertMessage(alertSet.getPointName(), value, false);
				}
			}
			}
		}
		
	}

}
