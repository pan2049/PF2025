package tw.pan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import tw.pan.utils.websocket.ControlWebsocketHandler;
import tw.pan.utils.websocket.ViewWebsocketHandler;

/**
 * [WebSocket] config
 * 
 * @author PAN
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private ViewWebsocketHandler viewWebsocketHandler;
	@Autowired
	private ControlWebsocketHandler controlWebsocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// [view]Websocket
		registry.addHandler(viewWebsocketHandler, "/api/view").setAllowedOrigins("*");
		// [control]Websocket
		registry.addHandler(controlWebsocketHandler, "/api/control").setAllowedOrigins("*");
	}

}
