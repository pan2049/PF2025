package tw.pan.service.impl.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.pan.dao.PointInfoDao;
import tw.pan.dao.RecordDao;
import tw.pan.entity.enumEntity.IoType;
import tw.pan.entity.po.DeviceState;
import tw.pan.entity.po.PointInfo;
import tw.pan.utils.SharedUtils;
import tw.pan.utils.cache.CustomKeyPair;
import tw.pan.utils.cache.DeviceStateCacheManager;
import tw.pan.utils.quartz.ScheduleMission;

@Service
public class RecordMissionService implements ScheduleMission {

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
					.getData(new CustomKeyPair<IoType.ViewType, Integer>(ioType.getViewType(), pointId));
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
