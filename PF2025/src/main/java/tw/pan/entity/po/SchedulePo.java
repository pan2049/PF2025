package tw.pan.entity.po;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulePo extends PointInfo{

	private Integer scheduleId;
	private String scheduleName;
	private Integer pointId;
	private Set<Integer> week;
	private String bootTime;
	private String stopTime;
	private String scheduleStatus;
	private String note;
}
