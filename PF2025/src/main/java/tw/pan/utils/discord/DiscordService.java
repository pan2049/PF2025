package tw.pan.utils.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tw.pan.utils.SharedUtils;

/**
 * Alert message push service
 * 
 * @author PAN
 */
@Component
public class DiscordService extends JDAService{

	@Value("${public.discord.token}")
	private String TOKEN;
	@Value("${public.discord.channel-id}")
	private String channelId;
	
	@Autowired
	public void init() {
		super.setToken(TOKEN).build();
	}
	
	/**
	 * send DIO alert message to discord channel
	 * 
	 * @param pointName		point name
	 * @param state			point state
	 * @param messageType	if true send Abnormal status message, else send Abnormal return message
	 */
	public void sendDIOAlertMessage(String pointName, Boolean state, Boolean messageType) {
		String message = "";
		String time = SharedUtils.getSystemTime();
		if(messageType) {
			message = "Point Name : "+ pointName +"\r\n"
					+ "Point State : "+ (state ? "Triggered/ON" : "Disconnect/OFF") +"\r\n"
					+ "Message : Abnormal status\r\n"
					+ "Time : " + time;
		}else {
			message = "Point Name : "+ pointName +"\r\n"
					+ "Point State : "+ (state ? "Triggered/ON" : "Disconnect/OFF") +"\r\n"
					+ "Message : Abnormal return\r\n"
					+ "Time : " + time;
		}
		super.sendMessageToChannel(message, channelId);
	}

	/**
	 * send AIO alert message to discord channel
	 * 
	 * @param pointName		point name
	 * @param value			point value
	 * @param messageType	if true send Abnormal status message, else send Abnormal return message
	 */
	public void sendAIOAlertMessage(String pointName, Float value, Boolean messageType) {
		String message = "";
		String time = SharedUtils.getSystemTime();
		if(messageType) {
			message = "Point Name : "+ pointName +"\r\n"
					+ "Point Value : "+ value +"\r\n"
					+ "Message : Abnormal value\r\n"
					+ "Time : " + time;
		}else {
			message = "Point Name : "+ pointName +"\r\n"
					+ "Point Value : "+ value +"\r\n"
					+ "Message : Abnormal return\r\n"
					+ "Time : " + time;
		}
		super.sendMessageToChannel(message, channelId);
	}
	
}
