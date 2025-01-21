package tw.jdi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.jdi.dao.AlertRecordDao;
import tw.jdi.dao.AlertSetDao;
import tw.jdi.entity.enumEntity.AlertStatus;
import tw.jdi.entity.enumEntity.IoType;
import tw.jdi.entity.enumEntity.IoType.SignalType;
import tw.jdi.entity.po.AlertRecord;
import tw.jdi.entity.po.AlertSet;
import tw.jdi.entity.po.DeviceState;
import tw.jdi.utils.SharedUtils;
import tw.jdi.utils.cache.Cache;
import tw.jdi.utils.cache.CacheKeyPair;
import tw.jdi.utils.cache.DeviceStateCacheManager;
import tw.jdi.utils.quartz.ScheduleMission;

@Service
public class AlertCheckService extends Cache<CacheKeyPair<SignalType, Integer>, Integer> implements ScheduleMission {

	@Autowired
	private AlertSetDao alertSetDao;
	@Autowired
	private AlertRecordDao alertRecordDao;
	@Autowired
	private DeviceStateCacheManager deviceStateCacheManager;
	
	@Autowired
	private void initMap() {
		List<AlertRecord> alertRecordListDIO = alertRecordDao.selectLastRecordFotInitDIO();
		List<AlertRecord> alertRecordListAIO = alertRecordDao.selectLastRecordFotInitAIO();
		if(!alertRecordListDIO.isEmpty()) {
			for(AlertRecord alertRecord : alertRecordListDIO) {
				super.setData(
						new CacheKeyPair<SignalType, Integer>(SignalType.DIGITAL, alertRecord.getPointId()), alertRecord.getAlertId());
			}
		}
		if(!alertRecordListAIO.isEmpty()) {
			for(AlertRecord alertRecord : alertRecordListAIO) {
				super.setData(
						new CacheKeyPair<SignalType, Integer>(SignalType.ANALOG, alertRecord.getPointId()), alertRecord.getAlertId());
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
		CacheKeyPair<IoType.ViewType, Integer> cacheKey = null;
		CacheKeyPair<IoType.SignalType, Integer> alertTagKey = null;
		for(AlertSet alertSet : alertSetListDIO) {
			pointId = alertSet.getPointId();
			ioType = alertSet.getIoType();
			alertStatus = alertSet.getAlertStatus();
			alertDefine = alertSet.getAlertDefine().equals(AlertStatus.ON) ? true : false;
			cacheKey = new CacheKeyPair<IoType.ViewType, Integer>(ioType.getViewType(), pointId);
			alertTagKey = new CacheKeyPair<IoType.SignalType, Integer>(ioType.getSignalType(), pointId);
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
					}
				}else {
					if(super.hasData(alertTagKey)) {
						Integer alertId = super.getData(alertTagKey);
						alertRecordDao.updateAlertRecordDIO(alertId, SharedUtils.getSystemTime());
						super.removeData(alertTagKey);
					}
				}
			}
			case OFF -> {
				if(super.hasData(alertTagKey)) {
					// 手動異常回歸, 更新紀錄並刪除Map資料
					Integer alertId = super.getData(alertTagKey);
					alertRecordDao.updateAlertRecordDIO(alertId, SharedUtils.getSystemTime());
					super.removeData(alertTagKey);
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
		CacheKeyPair<IoType.ViewType, Integer> cacheKey = null;
		CacheKeyPair<IoType.SignalType, Integer> alertTagKey = null;
		for(AlertSet alertSet : alertSetListAIO) {
			pointId = alertSet.getPointId();
			ioType = alertSet.getIoType();
			alertStatus = alertSet.getAlertStatus();
			alertUpper = alertSet.getAlertUpper();
			alertLower = alertSet.getAlertLower();
			cacheKey = new CacheKeyPair<IoType.ViewType, Integer>(ioType.getViewType(), pointId);
			alertTagKey = new CacheKeyPair<IoType.SignalType, Integer>(ioType.getSignalType(), pointId);
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
					}
				}else {
					if(super.hasData(alertTagKey)) {
						Integer alertId = super.getData(alertTagKey);
						alertRecordDao.updateAlertRecordAIO(alertId, SharedUtils.getSystemTime());
						super.removeData(alertTagKey);
					}
				}
			}
			case OFF -> {
				if(super.hasData(alertTagKey)) {
					// 手動異常回歸, 更新紀錄並刪除Map資料
					Integer alertId = super.getData(alertTagKey);
					alertRecordDao.updateAlertRecordAIO(alertId, SharedUtils.getSystemTime());
					super.removeData(alertTagKey);
				}
			}
			}
		}
		
	}

}
