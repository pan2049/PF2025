package tw.pan.entity.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import tw.pan.utils.SharedUtils;

@Getter
@JsonInclude(value = Include.NON_NULL)
public class ErrorDto {

	private String timestamp;
	private int statusCode;
	private String message;
	private String path;
	
	public ErrorDto() {
		this.timestamp = SharedUtils.getSystemTime();
	}
	
	public ErrorDto setStatus(Integer statusCode) {
		this.statusCode = statusCode;
		return this;
	}
	
	public ErrorDto setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public ErrorDto setPath(String path) {
		this.path = path;
		return this;
	}
	
}
