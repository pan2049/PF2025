package tw.jdi.entity.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SiteInfo {

	private Integer siteId;
	private String siteName;
	
}
