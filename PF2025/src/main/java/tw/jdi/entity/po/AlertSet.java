package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertSet extends PointInfo{

	private Integer setId;
	private Integer pointId;
	private AlertStatus alertStatus;
	private AlertStatus alertDefine;
	private Float alertUpper;
	private Float alertLower;
	private String updateTime;
	
	public enum AlertStatus{
		ON, OFF
	}
}
