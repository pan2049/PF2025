package tw.pan.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tw.pan.entity.enumEntity.AlertStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertSetDto {
	
	@NotNull
	private Integer pointId;
	@NotNull
	private AlertStatus alertStatus;
	private AlertStatus alertDefine;
	private Float alertUpper;
	private Float alertLower;
}
