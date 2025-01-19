package tw.jdi.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ControlDto {

	@NotNull
	private Integer pointId;
	private Boolean status;
	private Float value;
}
