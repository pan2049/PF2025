package tw.jdi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * [WebSocket]的配置
 * 
 * @author PAN
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

//	@Autowired
//	private MonitorPointWebSocketHandler monitorPointWebSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// [監視點]WebSocket -> 每秒傳送更新
//		registry.addHandler(monitorPointWebSocketHandler, "/api/monitor_point").setAllowedOrigins("*");

	}

}
