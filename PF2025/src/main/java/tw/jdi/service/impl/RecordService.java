package tw.jdi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.jdi.dao.PointInfoDao;
import tw.jdi.dao.RecordDao;
import tw.jdi.entity.enumEntity.IoType;
import tw.jdi.entity.po.DeviceState;
import tw.jdi.entity.po.PointInfo;
import tw.jdi.utils.SharedUtils;
import tw.jdi.utils.cache.CacheKeyPair;
import tw.jdi.utils.cache.DeviceStateCacheManager;
import tw.jdi.utils.quartz.ScheduleMission;

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
		List<PointInfo> pointList = pointInfoDao.selectAllPointId();
		DeviceState deviceState = null;
		IoType ioType = null;
		Integer pointId = null;
		Boolean state = null;
		Float value = null;
		for (PointInfo pointInfo : pointList) {
			pointId = pointInfo.getPointId();
			ioType = pointInfo.getIoType();
			deviceState = deviceStateCacheManager
					.getData(new CacheKeyPair<IoType.ViewType, Integer>(ioType.getViewType(), pointId));
			// 如果設備斷線掠過不紀錄
			if (deviceState == null) {
				continue;
			}
			if (deviceState.getDisconnect()) {
				continue;
			}
			switch(ioType) {
			case DI, DO -> {
				state = deviceState.getState();
				if(state != null) {
					recordDao.insertRecord(pointId, ioType, state, systemTime);
				}
			}
			case AI, AO -> {
				value = deviceState.getValue();
				if(value != null) {
					recordDao.insertRecord(pointId, ioType, value, systemTime);
				}
			}
			}
		}
	}

}
