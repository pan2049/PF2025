package tw.pan.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatewayInfo {

	private Integer gatewayId;
	private String gatewayName;
	private Integer siteId;
	private String ip;
	private Integer port;
}
