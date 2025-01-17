package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceInfo extends DeviceType{

	private Integer deviceId;
	private Integer typeId;
	private String deviceName;
	private Integer siteId;
	private String deviceInfo;
	
}
