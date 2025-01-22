package tw.pan.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tw.pan.entity.enumEntity.Arithmetic;
import tw.pan.entity.enumEntity.CommProtocol;
import tw.pan.entity.enumEntity.IoType;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointInfo extends DeviceInfo{

	private Integer pointId;
	private Integer deviceId;
	private String pointName;
	private CommProtocol comm;
	private IoType ioType;
	private Arithmetic arithmetic;
	private Float correct;
	private String unit;
}
