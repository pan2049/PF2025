package tw.pan.service.impl.schedule;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serotonin.modbus4j.ModbusMaster;

import jakarta.annotation.Resource;
import tw.pan.dao.GatewayInfoDao;
import tw.pan.dao.ModbusPointDao;
import tw.pan.entity.po.GatewayInfo;
import tw.pan.entity.po.ModbusPoint;
import tw.pan.utils.SharedUtils;
import tw.pan.utils.cache.DeviceStateCacheManager;
import tw.pan.utils.connect.Modbus4j;
import tw.pan.utils.quartz.ScheduleMission;

/**
 * Modbus設備讀取
 * 
 * @author PAN
 */

@Service
public class ModbusReadService implements ScheduleMission {

	@Resource(name = "schedulePool")
	private ScheduledExecutorService pool;
	@Autowired
	private DeviceStateCacheManager deviceStateCacheManager;
	@Autowired
	private GatewayInfoDao gatewayInfoDao;
	@Autowired
	private ModbusPointDao modbusPointDao;
	@Autowired
	private Modbus4j modbus4j;


	@Override
	public void doJob() {
		List<GatewayInfo> gatewayInfoList = gatewayInfoDao.selectAllGatewayInfo();
		for (GatewayInfo gatewayInfo : gatewayInfoList) {
			List<Integer> modbusSlaveList = modbusPointDao.selectModbusPointSlaveByGatewayId(gatewayInfo.getGatewayId());
			for (Integer modbusSlave : modbusSlaveList) {
				setSchedule(gatewayInfo, modbusSlave);
			}
		}
	}

	/**
	 * 獨立線程讀取 </br>
	 * 細分致設備站點，所以同一站點內部點數越多速度越慢
	 * 
	 * @param GatewayInfo gatewayInfo
	 * @param Integer     modbusSlave
	 */
	public void setSchedule(GatewayInfo gatewayInfo, Integer modbusSlave) {
		ModbusMaster modbusMaster = modbus4j.getLocalMaster(gatewayInfo.getIp(),
				gatewayInfo.getPort());
		pool.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				List<ModbusPoint> modbusPointList = modbusPointDao
						.selectModbusPointByGatewayAndSlave(gatewayInfo.getGatewayId(), modbusSlave);
				for (ModbusPoint modbusPoint : modbusPointList) {
					saveData(checkModbusMaster(modbusMaster, gatewayInfo), modbusPoint, modbusSlave);
				}
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	/**
	 * Modbus4j實際運用
	 * 
	 * @param ModbusMaster    master
	 * @param PointInfoModbus pointInfoModbus
	 * @param Integer         slave
	 */
	public void saveData(ModbusMaster master, ModbusPoint modbusPoint, Integer slave) {
		switch (modbusPoint.getFunction()) {
		case COIL -> {
			Boolean coilsValue = modbus4j.readCoils(master, slave, modbusPoint.getAddress());
			// save cache
			if (coilsValue == null) {
				deviceStateCacheManager.setDisconnect(modbusPoint);
			} else {
				deviceStateCacheManager.setCache(modbusPoint, coilsValue);
			}			
		}
		case DISCRETE -> {
			Boolean discreteValue = modbus4j.readStatus(master, slave, modbusPoint.getAddress());
			// save cache
			if (discreteValue == null) {
				deviceStateCacheManager.setDisconnect(modbusPoint);
			} else {
				deviceStateCacheManager.setCache(modbusPoint, discreteValue);
			}			
		}
		case HOLDING -> {
			BigDecimal holdingValue = modbus4j.readHoldingRegister(master, slave, modbusPoint.getAddress(),
					modbusPoint.getFormat());
			// save cache
			if (holdingValue == null) {
				deviceStateCacheManager.setDisconnect(modbusPoint);
			} else {
				deviceStateCacheManager.setCache(modbusPoint, SharedUtils.convertValue(holdingValue,
						modbusPoint.getArithmetic(), modbusPoint.getCorrect()));
			}			
		}
		case INPUT -> {
			BigDecimal inputValue = modbus4j.readInputRegister(master, slave, modbusPoint.getAddress(),
					modbusPoint.getFormat());
			// save cache
			if (inputValue == null) {
				deviceStateCacheManager.setDisconnect(modbusPoint);
			} else {
				deviceStateCacheManager.setCache(modbusPoint, SharedUtils.convertValue(inputValue,
						modbusPoint.getArithmetic(), modbusPoint.getCorrect()));
			}			
		}
		}
	}

	/**
	 * 不斷線除除非沒有連線，重新連線
	 * 
	 * @param ModbusMaster  modbusMaster
	 * @param GatewayModbus gatewayModbus
	 * @return ModbusMaster
	 */
	public ModbusMaster checkModbusMaster(ModbusMaster modbusMaster, GatewayInfo gatewayInfo) {
		if (modbusMaster == null || !modbusMaster.isConnected()) {
			return modbus4j.getLocalMaster(gatewayInfo.getIp(), gatewayInfo.getPort());
		} else {
			return modbusMaster;
		}
	}

}
