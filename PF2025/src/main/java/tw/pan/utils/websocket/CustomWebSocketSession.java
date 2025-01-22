package tw.pan.utils.websocket;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket[Cache] -> for control point websocket cache
 * 
 * @author PAN
 */

public class CustomWebSocketSession{

	private boolean initSign = false;
	private WebSocketSession session;

	public boolean isInit() {
		return initSign;
	}

	public CustomWebSocketSession init() {
		this.initSign = true;
		return this;
	}

	public CustomWebSocketSession reset() {
		this.initSign = false;
		return this;
	}

	public WebSocketSession getSession() {
		return this.session;
	}

	public CustomWebSocketSession setSession(WebSocketSession session) {
		this.session = session;
		return this;
	}

}
