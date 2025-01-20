package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tw.jdi.entity.enumEntity.Arithmetic;
import tw.jdi.entity.enumEntity.IoType;

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
}
