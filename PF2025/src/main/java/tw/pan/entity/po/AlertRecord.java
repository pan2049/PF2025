package tw.pan.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tw.pan.entity.enumEntity.AlertStatus;

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
