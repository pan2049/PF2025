package tw.jdi.utils.websocket;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import tw.jdi.utils.SharedUtils;
import tw.jdi.utils.cache.Cache;

@Component
public class ControlWebsocketCache extends Cache<String, CustomWebSocketSession>{

	/**
	 * send message to all channel
	 * 
	 * @param message
	 */
	public void sendMessageToAllChannel(String message) {
		for(CustomWebSocketSession customSession : super.getAllValue()) {
			try {
				customSession.init().getSession().sendMessage(new TextMessage(message));			
			} catch (IOException e) {
				SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
			}
		}
	}
	
	/**
	 * uninit channel
	 * 
	 * @param message
	 */
	public void sendMessageToUninitChannel(String message) {
		for(CustomWebSocketSession customSession : super.getAllValue()) {
			if(customSession.isInit()) {
				continue;
			}
			try {
				customSession.init().getSession().sendMessage(new TextMessage(message));			
			} catch (IOException e) {
				SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
			}
		}
	}
	
}
