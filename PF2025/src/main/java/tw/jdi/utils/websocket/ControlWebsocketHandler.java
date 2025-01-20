package tw.jdi.utils.websocket;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * control point websocket handler
 */
@Component
public class ControlWebsocketHandler extends TextWebSocketHandler{

	@Autowired
	private ControlWebsocketCache controlWebsocketCache;
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		if (message == null) {
			return;
		}
		// use message to to something ...
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		System.out.println(session.getId() + " -> Connection Established");
		controlWebsocketCache.setData(session.getId(), new CustomWebSocketSession().setSession(session));
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
		session.close();
		controlWebsocketCache.removeData(session.getId());
		if (status.getReason() != null) {
			System.out.println(session.getId() + " -> Connection Closed");
		}
	}
}
