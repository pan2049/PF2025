package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointInfo extends DeviceInfo{

	private Integer pointId;
	private Integer deviceId;
	private String pointName;
	private IoType ioType;
	private Arithmetic arithmetic;
	private Float correct;
	private String unit;
	
	public enum IoType{
		DI, DO, AI, AO
	}
	
	public enum Arithmetic{
		ADD, SUB, MULT, DIV
	}
}
