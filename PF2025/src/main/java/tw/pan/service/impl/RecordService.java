package tw.pan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.pan.dao.PointInfoDao;
import tw.pan.dao.RecordDao;
import tw.pan.entity.dto.RecordDto;
import tw.pan.entity.po.PointInfo;
import tw.pan.entity.po.RecordPo;
import tw.pan.utils.cache.CustomKeyPair;

@Service
public class RecordService {

	@Autowired
	private PointInfoDao pointInfoDao;
	@Autowired
	private RecordDao recordDao;
	
	public Map<CustomKeyPair<String, String>, List<RecordPo>> getRecord(RecordDto recordDto) {
		Map<CustomKeyPair<String, String>, List<RecordPo>> result = new HashMap<>();
		for(Integer pointId : recordDto.getPointIdSet()) {
			PointInfo pointInfo = pointInfoDao.selectPointInfoById(pointId);
			if(pointInfo == null) {
				continue;
			}
			result.put(
					new CustomKeyPair<String, String>(pointInfo.getDeviceName(), pointInfo.getUnit()), 
					recordDao.selectRecordByDate(pointId, pointInfo.getIoType(), recordDto.getStartTime(), recordDto.getEndTime()));
		}
		return result;
	}
}
