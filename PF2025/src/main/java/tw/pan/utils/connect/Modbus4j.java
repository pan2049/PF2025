package tw.pan.utils.connect;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

import tw.pan.entity.po.ModbusPoint.ModbusFormat;
import tw.pan.utils.SharedUtils;


/**
 * [Modbus4j]連線工具，已改寫過，將斷線移到外面去自行決定斷線時機，測試版
 * 
 * @author PAN
 */

@Component
public class Modbus4j {

	/**
	 * 建立modbus連線，取得ModbusMaster
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	public ModbusMaster getLocalMaster(String ip, int port) {
		ModbusFactory modbusFactory = new ModbusFactory();
		IpParameters params = new IpParameters();
		params.setHost(ip);
		params.setPort(port);
		ModbusMaster master = modbusFactory.createTcpMaster(params, false);
		master.setTimeout(5000);
		try {
			master.init();
			return master;
		} catch (ModbusInitException e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * 讀取線圈狀態 fun 01 readCoils
	 * 
	 * @param modbusMastar
	 * @param slaveId
	 * @param offset
	 * @return
	 */
	public Boolean readCoils(ModbusMaster modbusMastar, int slaveId, int offset) {
		Boolean result = null;
		try {
			BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
			result = modbusMastar.getValue(loc);
		} catch (ModbusTransportException | ErrorResponseException e) {
			catchConsole(slaveId, offset, e);
		}
		return result;
	}

	/**
	 * 讀取線圈狀態 fun 02 input Status
	 * 
	 * @param modbusMastar
	 * @param slaveId
	 * @param offset
	 * @return
	 */
	public Boolean readStatus(ModbusMaster modbusMastar, int slaveId, int offset) {
		Boolean result = null;
		try {
			BaseLocator<Boolean> loc = BaseLocator.inputStatus(slaveId, offset);
			result = modbusMastar.getValue(loc);
		} catch (ModbusTransportException | ErrorResponseException e) {
			catchConsole(slaveId, offset, e);
		}
		return result;
	}

	/**
	 * 讀取數值狀態 fun 03 readHoldingRegisters 讀取16bit & 32bit皆可
	 * 
	 * @param modbusMastar
	 * @param slaveId
	 * @param offset
	 * @param format
	 * @return
	 */
	public BigDecimal readHoldingRegister(ModbusMaster modbusMastar, int slaveId, int offset, ModbusFormat format) {
		BigDecimal result = null;
		try {
			BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, getDataType(format));
			Number value = modbusMastar.getValue(loc);
			result = new BigDecimal(value.doubleValue()).setScale(2, RoundingMode.HALF_UP);
		} catch (ModbusTransportException | ErrorResponseException e) {
			catchConsole(slaveId, offset, e);
		}
		return result;
	}

	/**
	 * 讀取數值狀態 fun 04 readInputRegisters 讀取16bit & 32bit皆可
	 * 
	 * @param modbusMastar
	 * @param slaveId
	 * @param offset
	 * @param format
	 * @return
	 */
	public BigDecimal readInputRegister(ModbusMaster modbusMastar, int slaveId, int offset, ModbusFormat format) {
		BigDecimal result = null;
		try {
			BaseLocator<Number> loc = BaseLocator.inputRegister(slaveId, offset, getDataType(format));
			Number value = modbusMastar.getValue(loc);
			result = new BigDecimal(value.doubleValue()).setScale(2, RoundingMode.HALF_UP);
		} catch (ModbusTransportException | ErrorResponseException e) {
			catchConsole(slaveId, offset, e);
		}
		return result;
	}

	/**
	 * 寫入狀態方法 1.0
	 * 
	 * @param modbusMastar
	 * @param slaveId
	 * @param offset
	 * @param format
	 * @param value
	 * @throws ErrorResponseException
	 * @throws ModbusTransportException
	 */
	public void writeCoil(ModbusMaster modbusMastar, int slaveId, int offset, ModbusFormat format, boolean value) {
		try {
			BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
			modbusMastar.setValue(loc, value);
		} catch (ModbusTransportException | ErrorResponseException e) {
			catchConsole(slaveId, offset, e);
		}
	}

	/**
	 * 寫入狀態方法 2.0</br>
	 * 新加如果有發生錯誤會自動重試功能 </br>
	 * 最多重試兩次，間格1000豪秒
	 * 
	 * @param ip
	 * @param port
	 * @param slaveId
	 * @param offset
	 * @param format
	 * @param value
	 * @throws ModbusInitException
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 */
	@Retryable(retryFor = { ModbusInitException.class, ModbusTransportException.class,
			ErrorResponseException.class }, maxAttempts = 2, backoff = @Backoff(delay = 1000))
	public void writeCoil(String ip, int port, int slaveId, int offset, ModbusFormat format, boolean value)
			throws ModbusInitException, ModbusTransportException, ErrorResponseException {
		ModbusFactory modbusFactory = new ModbusFactory();
		IpParameters params = new IpParameters();
		params.setHost(ip);
		params.setPort(port);
		ModbusMaster master = modbusFactory.createTcpMaster(params, false);
		master.setTimeout(5000);
		master.init();
		BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
		master.setValue(loc, value);
		master.destroy();
	}

	/**
	 * 寫入數值方法 1.0
	 * 
	 * @param modbusMastar
	 * @param slaveId
	 * @param offset
	 * @param format
	 * @param value
	 * @throws ErrorResponseException
	 * @throws ModbusTransportException
	 */
	public void writeHoldingRegister(ModbusMaster modbusMastar, int slaveId, int offset, ModbusFormat format, Number value) {
		try {
			BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, getDataType(format));
			modbusMastar.setValue(loc, value);
		} catch (ModbusTransportException | ErrorResponseException e) {
			catchConsole(slaveId, offset, e);
		}
	}

	/**
	 * 寫入數值方法 2.0 </br>
	 * 新加如果有發生錯誤會自動重試功能 </br>
	 * 最多重試兩次，間格1000豪秒
	 * 
	 * @param ip
	 * @param port
	 * @param slaveId
	 * @param offset
	 * @param format
	 * @param value
	 * @throws ModbusInitException
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 */
	@Retryable(retryFor = { ModbusInitException.class, ModbusTransportException.class,
			ErrorResponseException.class }, maxAttempts = 2, backoff = @Backoff(delay = 1000))
	public void writeHoldingRegister(String ip, int port, int slaveId, int offset, ModbusFormat format, Number value)
			throws ModbusInitException, ModbusTransportException, ErrorResponseException {
		ModbusFactory modbusFactory = new ModbusFactory();
		IpParameters params = new IpParameters();
		params.setHost(ip);
		params.setPort(port);
		ModbusMaster master = modbusFactory.createTcpMaster(params, false);
		master.setTimeout(5000);
		master.init();
		BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, getDataType(format));
		master.setValue(loc, value);
		master.destroy();
	}

	/**
	 * 關閉modbus連線，傳入欲關閉之ModbusMaster
	 * 
	 * @param modbusMastar
	 */
	public void disconnectMaster(ModbusMaster modbusMastar) {
		modbusMastar.destroy();
	}

	/**
	 * 傳入Format 判斷屬於哪種DataType
	 * 
	 * @param format
	 * @return DataType
	 */
	public int getDataType(ModbusFormat format) {
		int result = 0;
		switch (format) {
		case SINGNED:
			result = DataType.TWO_BYTE_INT_SIGNED;
			break;
		case UNSINGNED:
			result = DataType.TWO_BYTE_INT_UNSIGNED;
			break;
		case FLOAT_ABCD:
			result = DataType.FOUR_BYTE_FLOAT;
			break;
		case FLOAT_CDAB:
			result = DataType.FOUR_BYTE_FLOAT_SWAPPED;
			break;
		case LONG_ABCD:
			result = DataType.FOUR_BYTE_INT_UNSIGNED;
			break;
		case LONG_CDAB:
			result = DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED;
		}

		return result;
	}

	/**
	 * 列印異常
	 * 
	 * @param slaveId
	 * @param offset
	 * @param e
	 */
	public void catchConsole(int slaveId, int offset, Exception e) {
		SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
	}

}
