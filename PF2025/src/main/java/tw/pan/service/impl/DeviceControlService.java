package tw.pan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import tw.pan.dao.ModbusPointDao;
import tw.pan.dao.PointInfoDao;
import tw.pan.entity.dto.ControlDto;
import tw.pan.entity.po.ModbusPoint;
import tw.pan.entity.po.PointInfo;
import tw.pan.utils.connect.Bacnet4j;
import tw.pan.utils.connect.Modbus4j;
import tw.pan.utils.exception.DatabaseOperateException;
import tw.pan.utils.exception.RequestErrorException;

@Service
public class DeviceControlService {

	@Autowired
	private Modbus4j modbus4j;
	@Autowired
	private Bacnet4j bacnet4j;
	@Autowired
	private PointInfoDao pointInfoDao;
	@Autowired
	private ModbusPointDao modbusPointDao;
//	@Autowired
//	private BacnetPointDao bacnetPointDao;
	
	
	public Boolean control(ControlDto controlDto) throws RequestErrorException, DatabaseOperateException, 
	ModbusInitException, ModbusTransportException, ErrorResponseException {
		PointInfo pointInfo = pointInfoDao.selectPointInfoById(controlDto.getPointId());
		if(pointInfo == null) throw new RequestErrorException("point info not found");
		Integer pointId = pointInfo.getPointId();
		switch(pointInfo.getComm()) {
		case MODBUS -> {
			ModbusPoint modbusPoint = modbusPointDao.selectModbusPointByPointId(pointId);
			if(modbusPoint == null) throw new DatabaseOperateException("modbus point info not found");
			switch(pointInfo.getIoType().getSignalType()) {
			case DIGITAL -> {
				if(controlDto.getStatus() == null) throw new RequestErrorException("request data error");
				modbus4j.writeCoil(modbusPoint.getIp(), modbusPoint.getPort(), modbusPoint.getSlave(), 
						modbusPoint.getAddress(), modbusPoint.getFormat(), controlDto.getStatus());
			}
			case ANALOG -> {
				if(controlDto.getValue() == null) throw new RequestErrorException("request data error");
				modbus4j.writeHoldingRegister(modbusPoint.getIp(), modbusPoint.getPort(), modbusPoint.getSlave(), 
						modbusPoint.getAddress(), modbusPoint.getFormat(), controlDto.getValue());
			}
			}
		}
		case BACNET -> {
		}
		}
		
		return true;
	}
	
	
}
