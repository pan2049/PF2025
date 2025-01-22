package tw.pan.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import tw.pan.utils.SharedUtils;

@Getter
@JsonInclude(value = Include.NON_NULL)
public class AccessDto {

	private String timestamp;
	private Boolean success;
	private String jwt;
	
	public AccessDto() {
		this.timestamp = SharedUtils.getSystemTime();
	}

	public AccessDto setSuccess(Boolean success) {
		this.success = success;
		return this;
	}
	
	public AccessDto setJwt(String token) {
		this.jwt = token;
		return this;
	}
	
}
