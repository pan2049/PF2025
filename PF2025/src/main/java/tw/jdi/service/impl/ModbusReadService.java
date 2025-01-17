package tw.jdi.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serotonin.modbus4j.ModbusMaster;

import jakarta.annotation.Resource;
import tw.jdi.service.ScheduleMission;
import tw.jdi.utils.connect.Modbus4j;

/**
 * Modbus設備讀取
 * 
 * @author PAN
 */

@Service
public class ModbusReadService implements ScheduleMission {

	@Resource(name = "schedulePool")
	private ScheduledExecutorService pool;
//	@Autowired
//	private GatewayModbusDao gatewayModbusDao;
//	@Autowired
//	private PointInfoModbusDao pointInfoModbusDao;
//	@Autowired
//	private Modbus4j modbus4j;
//	@Autowired
//	private DeviceStateCacheManager deviceStateCacheManager;

	@Override
	public void doJob() {
//		List<GatewayModbus> gatewayModbusList = gatewayModbusDao.selectGatewayModbus();
//		for (GatewayModbus gatewayModbus : gatewayModbusList) {
//			List<Integer> modbusSlaveList = pointInfoModbusDao.selectModbusSlave(gatewayModbus.getModbusNum());
//			for (Integer modbusSlave : modbusSlaveList) {
//				setSchedule(gatewayModbus, modbusSlave);
//			}
//		}
	}

//	/**
//	 * 獨立線程讀取 </br>
//	 * 細分致設備站點，所以同一站點內部點數越多速度越慢
//	 * 
//	 * @param GatewayModbus gatewayModbus
//	 * @param Integer       modbusSlave
//	 */
//	public void setSchedule(GatewayModbus gatewayModbus, Integer modbusSlave) {
//		ModbusMaster modbusMaster = modbus4j.getLocalMaster(gatewayModbus.getModbusIpv4(),
//				gatewayModbus.getModbusPort());
//		pool.scheduleWithFixedDelay(new Runnable() {
//			@Override
//			public void run() {
//				List<PointInfoModbus> pointInfoModbusList = pointInfoModbusDao
//						.selectPointModbus(gatewayModbus.getModbusNum(), modbusSlave);
//				for (PointInfoModbus pointInfoModbus : pointInfoModbusList) {
//					saveData(checkModbusMaster(modbusMaster, gatewayModbus), pointInfoModbus, modbusSlave);
//				}
//			}
//		}, 0, 100, TimeUnit.MILLISECONDS);
//	}
//
//	/**
//	 * Modbus4j實際運用
//	 * 
//	 * @param ModbusMaster    master
//	 * @param PointInfoModbus pointInfoModbus
//	 * @param Integer         slave
//	 */
//	public void saveData(ModbusMaster master, PointInfoModbus pointInfoModbus, Integer slave) {
//		switch (pointInfoModbus.getModbusFunction()) {
//		case coils:
//			Boolean coilsValue = modbus4j.readCoils(master, slave, pointInfoModbus.getModbusAddress());
//			// save cache
//			if (coilsValue == null) {
//				deviceStateCacheManager.setDisconnect(pointInfoModbus);
//			} else {
//				deviceStateCacheManager.setCache(pointInfoModbus, coilsValue);
//			}
//			break;
//		case discrete:
//			Boolean discreteValue = modbus4j.readStatus(master, slave, pointInfoModbus.getModbusAddress());
//			// save cache
//			if (discreteValue == null) {
//				deviceStateCacheManager.setDisconnect(pointInfoModbus);
//			} else {
//				deviceStateCacheManager.setCache(pointInfoModbus, discreteValue);
//			}
//			break;
//		case holding:
//			BigDecimal holdingValue = modbus4j.readHoldingRegister(master, slave, pointInfoModbus.getModbusAddress(),
//					pointInfoModbus.getModbusFormat());
//			// save cache
//			if (holdingValue == null) {
//				deviceStateCacheManager.setDisconnect(pointInfoModbus);
//			} else {
//				deviceStateCacheManager.setCache(pointInfoModbus, SharedUtils.convertValue(holdingValue,
//						pointInfoModbus.getCalSign(), pointInfoModbus.getConvertValue()));
//			}
//			break;
//		case input:
//			BigDecimal inputValue = modbus4j.readInputRegister(master, slave, pointInfoModbus.getModbusAddress(),
//					pointInfoModbus.getModbusFormat());
//			// save cache
//			if (inputValue == null) {
//				deviceStateCacheManager.setDisconnect(pointInfoModbus);
//			} else {
//				deviceStateCacheManager.setCache(pointInfoModbus, SharedUtils.convertValue(inputValue,
//						pointInfoModbus.getCalSign(), pointInfoModbus.getConvertValue()));
//			}
//			break;
//		}
//	}
//
//	/**
//	 * 不斷線除除非沒有連線，重新連線
//	 * 
//	 * @param ModbusMaster  modbusMaster
//	 * @param GatewayModbus gatewayModbus
//	 * @return ModbusMaster
//	 */
//	public ModbusMaster checkModbusMaster(ModbusMaster modbusMaster, GatewayModbus gatewayModbus) {
//		if (modbusMaster == null || !modbusMaster.isConnected()) {
//			return modbus4j.getLocalMaster(gatewayModbus.getModbusIpv4(), gatewayModbus.getModbusPort());
//		} else {
//			return modbusMaster;
//		}
//	}

}
