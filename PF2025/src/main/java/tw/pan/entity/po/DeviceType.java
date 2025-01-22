package tw.pan.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceType {

	private Integer typeId;
	private String typeName;
	private String typeInfo;
}
