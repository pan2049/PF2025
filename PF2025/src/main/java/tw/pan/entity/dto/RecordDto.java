package tw.pan.entity.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordDto {

	@NotNull
	private Set<Integer> pointIdSet;
	@NotNull
	private String startTime;
	private String endTime;
}
