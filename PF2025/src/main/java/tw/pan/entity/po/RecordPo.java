package tw.pan.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordPo {

	private Integer recordId;
	private String Status;
	private Float value;
	private String time;
}
