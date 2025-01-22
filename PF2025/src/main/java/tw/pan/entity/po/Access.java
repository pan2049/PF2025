package tw.pan.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Access {

	private Integer accessId;
	private String username;
	private String access;
	private String password;
	private String updateTime;
}
