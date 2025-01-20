package tw.jdi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.jdi.dao.RecordDao;
import tw.jdi.service.ScheduleMission;
import tw.jdi.utils.SharedUtils;
import tw.jdi.utils.cache.DeviceStateCacheManager;

@Service
public class RecordService implements ScheduleMission {

	@Autowired
	private DeviceStateCacheManager deviceStateCacheManager;
	@Autowired
	private PointInfoDao pointInfoDao;
	@Autowired
	private RecordDao recordDao;
	
	@Override
	public void doJob() {
		String systemTime = SharedUtils.getSystemTimeSecondZero();
		List<PointInfo> pointList = pointInfoDao.selectPointInfo();
		DeviceState deviceState = null;
		String systemName = null;
		String areaName = null;
		SignalType signalType = null;
		Integer pointNum = null;
		try {
			for (PointInfo pointInfo : pointList) {
				systemName = pointInfo.getSystemName();
				areaName = pointInfo.getAreaName();
				signalType = pointInfo.getSignalType();
				pointNum = pointInfo.getPointNum();
				deviceState = deviceStateCacheManager
						.getData(new CacheKeyPair(systemName, areaName, signalType.getType(), pointNum));
				// 如果設備斷線掠過不紀錄
				if (deviceState == null) {
					continue;
				}
				if (deviceState.getDisconnect()) {
					continue;
				}
				switch (signalType) {
				case DI, DO:
					// 判斷是否程式剛啟動還沒連上線 狀態
					if (deviceState.getState() == null) {
						continue;
					}
					recordGeneralDao.insertRecordGeneral(pointNum, systemTime, deviceState.getState(), null);
					break;
				case AI, AO:
					// 判斷是否程式剛啟動還沒連上線 數值
					if (deviceState.getValue() == null) {
						continue;
					}
					recordGeneralDao.insertRecordGeneral(pointNum, systemTime, null,
							deviceState.getValue().doubleValue());
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
		}
	}

}
