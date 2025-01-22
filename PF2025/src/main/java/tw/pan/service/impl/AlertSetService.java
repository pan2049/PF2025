package tw.pan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.pan.dao.AlertSetDao;
import tw.pan.dao.PointInfoDao;
import tw.pan.entity.dto.AlertSetDto;
import tw.pan.entity.po.PointInfo;
import tw.pan.utils.exception.DatabaseOperateException;
import tw.pan.utils.exception.RequestErrorException;

@Service
public class AlertSetService {

	@Autowired
	private PointInfoDao pointInfoDao;
	@Autowired
	private AlertSetDao alertSetDao;
	
	@Transactional(rollbackFor = DatabaseOperateException.class)
	public void editAlertSet(AlertSetDto alertSetDto) throws RequestErrorException {
		PointInfo pointInfo = pointInfoDao.selectPointInfoById(alertSetDto.getPointId());
		if(pointInfo == null) throw new RequestErrorException("point info not found");
		Integer pointId = pointInfo.getPointId();
		switch(pointInfo.getIoType().getSignalType()) {
		case DIGITAL -> {
			if(alertSetDto.getAlertDefine() == null) throw new RequestErrorException("request's param alertDefine is null");
			alertSetDao.updateAlertSetDIO(pointId, alertSetDto.getAlertStatus(), alertSetDto.getAlertDefine());
		}
		case ANALOG -> {
			if(alertSetDto.getAlertUpper() == null || alertSetDto.getAlertLower() == null) 
				throw new RequestErrorException("request's param alertUpper or alertLower is null");
			alertSetDao.updateAlertSetAIO(pointId, alertSetDto.getAlertStatus(), alertSetDto.getAlertUpper(), alertSetDto.getAlertLower());
		}
		}
	
	}
}
