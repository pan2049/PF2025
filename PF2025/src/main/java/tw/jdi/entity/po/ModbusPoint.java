package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModbusPoint extends PointInfo{

	private Integer modbusId;
	private Integer gatewayId;
	private Integer pointId;
	private ModbusFunction function;
	private Integer slave;
	private Integer address;
	private ModbusFormat format;
	
	public enum ModbusFunction{
		COIL, DISCRETE, INPUT, HOLDING
	}
	
	public enum ModbusFormat{
		SINGNED, UNSINGNED, FLOAT_ABCD, FLOAT_CDAB, LONG_ABCD, LONG_CDAB
	}
}
