//package tw.jdi.utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//
///**
// * Websocket[Handler] -> [監視點]
// * 
// * @author PAN
// */
//
//@Component
//public class MonitorPointWebSocketHandler extends CustomWebSocketHandler {
//
//	@Autowired
//	private MonitorPointWebSocketCache monitorPointWebSocketCache;
//
//	@Override
//	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//		if (message == null) {
//			return;
//		}
//		JSONObject jsonObject = new JSONObject(message.getPayload());
//		if (jsonObject.has("system_name") && jsonObject.has("area_name")) {
//			monitorPointWebSocketCache.getData(session.getId())
//					.setRequestParam(getParam(jsonObject.getString("system_name"), jsonObject.getString("area_name")));
//		}
//	}
//
//	@Override
//	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//		SharedUtils.getLogger(this.getClass()).info(session.getId() + " -> Connection Established");
//		monitorPointWebSocketCache.setData(session.getId(), new CustomWebSocketCache().setSession(session));
//	}
//
//	@Override
//	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		session.close();
//		monitorPointWebSocketCache.removeData(session.getId());
//		if (status.getReason() != null) {
//			SharedUtils.getLogger(this.getClass()).info(session.getId() + " -> Connection Closed");
//		}
//	}
//
//}
