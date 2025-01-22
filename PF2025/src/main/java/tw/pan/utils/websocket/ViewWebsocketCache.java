package tw.pan.utils.websocket;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import tw.pan.utils.SharedUtils;
import tw.pan.utils.cache.Cache;

/**
 * view point websocket connect cache
 * 
 * @author PAN
 */
@Component
public class ViewWebsocketCache extends Cache<String, WebSocketSession>{

	/**
	 * send message to all channel
	 * 
	 * @param message
	 */
	public void sendMessageToAllChannel(String message) {
		for(WebSocketSession session : super.getAllValue()) {
			try {
				session.sendMessage(new TextMessage(message));
			} catch (IOException e) {
				SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
			}
		}
	}
	
}
