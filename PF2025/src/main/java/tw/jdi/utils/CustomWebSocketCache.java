package tw.jdi.utils;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket[Cache]
 * 
 * @author PAN
 */

public class CustomWebSocketCache {

	private boolean initSign = false;
	private String requestParam = "";
	private WebSocketSession session;

	public boolean isInit() {
		return initSign;
	}

	public CustomWebSocketCache init() {
		this.initSign = true;
		return this;
	}

	public CustomWebSocketCache reset() {
		this.initSign = false;
		return this;
	}

	public WebSocketSession getSession() {
		return this.session;
	}

	public CustomWebSocketCache setSession(WebSocketSession session) {
		this.session = session;
		return this;
	}

	public CustomWebSocketCache setRequestParam(String requestParam) {
		this.requestParam = requestParam;
		return this;
	}

	public boolean isRequestParam(String systemArea) {
		return this.requestParam.equals(systemArea) ? true : false;
	}

}
