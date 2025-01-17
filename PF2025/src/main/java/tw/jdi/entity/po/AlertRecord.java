package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tw.jdi.entity.po.AlertSet.AlertStatus;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertRecord extends PointInfo{

	private Integer alertId;
	private Integer pointId;
	private AlertStatus alertStatus;
	private Float alertValue;
	private String startTime;
	private String returnTime;
	private String updateTime;
}
