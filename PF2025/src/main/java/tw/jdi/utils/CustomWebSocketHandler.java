package tw.jdi.utils;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket[Handler]
 * 
 * @author PAN
 */

public class CustomWebSocketHandler extends TextWebSocketHandler {

	/**
	 * 功能 -> 連接[失敗]後執行
	 * 
	 * @param WebSocketSession session
	 * @param Exception        e
	 * @author PAN
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable e) {
//		SharedUtils.getLogger(e).error(e.getLocalizedMessage());
	}

	/**
	 * 功能 -> 取得[參數]
	 * 
	 * @param String systemName
	 * @param String areaName
	 * @return String
	 * @author PAN
	 */
	public String getParam(String systemName, String areaName) {
		return systemName + areaName;
	}

	/**
	 * 功能 -> 取得[系統名稱]
	 * 
	 * @param String url
	 * @return String
	 * @author PAN
	 */
	public String getSystemName(String url) {
		String[] urlArray = url.split("/");
		return urlArray[urlArray.length - 1];
	}

}
