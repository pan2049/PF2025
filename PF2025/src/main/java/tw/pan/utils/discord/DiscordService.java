package tw.pan.utils.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscordService extends JDAService{

	@Value("${public.token.discord}")
	private String TOKEN;
	
	@Autowired
	public void init() {
		super.setToken(TOKEN).build();
	}
	

}
