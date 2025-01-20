package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 點位即時值內存資料格式 { 點位編號:DeviceState.class 10:{state: true, alert:
 * 0,disconnect:false} 11:{state: false, alert: 0, disconnect: false} }
 * 
 * @author PAN
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceState {

	@JsonProperty(value = "state")
	private Boolean state;
	@JsonProperty(value = "value")
	private Float value;
	@JsonProperty(value = "alert")
	private Integer alert;

	@JsonProperty(value = "disconnect")
	private Boolean disconnect = false;
	@JsonProperty(value = "step")
	private Integer step = 0;

	public DeviceState setState(Boolean state) {
		this.state = state;
		disconnect = false;
		step = 0;
		return this;
	}

	public DeviceState setValue(Float value) {
		this.value = value;
		disconnect = false;
		step = 0;
		return this;
	}

	public DeviceState disconnectStep() {
		step += 1;
		if (step > 5) {
			disconnect = true;
		}
		return this;
	}

}
