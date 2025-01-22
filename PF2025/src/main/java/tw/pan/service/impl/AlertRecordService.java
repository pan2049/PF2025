package tw.pan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.pan.dao.AlertRecordDao;
import tw.pan.dao.PointInfoDao;
import tw.pan.entity.dto.RecordDto;
import tw.pan.entity.po.AlertRecord;
import tw.pan.entity.po.PointInfo;
import tw.pan.utils.cache.CustomKeyPair;

@Service
public class AlertRecordService {

	@Autowired
	private PointInfoDao pointInfoDao;
	@Autowired
	private AlertRecordDao alertRecordDao;
	
	public Map<CustomKeyPair<String, String>, List<AlertRecord>> getAlertRecord(RecordDto recordDto) {
		Map<CustomKeyPair<String, String>, List<AlertRecord>> result = new HashMap<>();
		for(Integer pointId : recordDto.getPointIdSet()) {
			PointInfo pointInfo = pointInfoDao.selectPointInfoById(pointId);
			if(pointInfo == null) {
				continue;
			}
			result.put(
					new CustomKeyPair<String, String>(pointInfo.getPointName(), pointInfo.getUnit()), 
					alertRecordDao.selectAlertRecordByPointId(pointId, pointInfo.getIoType(), recordDto.getStartTime(), recordDto.getEndTime()));
		}
		return result;
	}
}
