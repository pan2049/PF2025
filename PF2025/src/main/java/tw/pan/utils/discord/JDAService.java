package tw.pan.utils.discord;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

/**
 * Discord JDA Service 實作工具 </br>
 * 請用繼承的方式 </br>
 * 記得搭配資料庫初始化channelMap，或是運用ChannelEditListener來做自動化管理都行 </br>
 * 提供服務的實作建立與基本方法的實作，其餘需求請自行修改 </br>
 * 
 * ※註: 有特殊需求需修改時，請優先在繼承之子類別增加，除非效能優於父類別目前之方法
 * 
 * @author PAN
 */
public class JDAService {

	private String TOKEN;
	private JDA jda;
	private CommandListUpdateAction commands;
	private Map<String, MessageChannel> channelMap = new ConcurrentHashMap<>();
	
	/**
	 * 設定token </br>
	 * ※一定要給
	 */
	public JDAService setToken(String token) {
		this.TOKEN = token;
		return this;
	}
	
	/**
	 * 建立JDA物件 </br>
	 * 一定要先設定token喔
	 * 
	 * @throws RejectedExecutionException
	 * @throws InvalidTokenException
	 * @throws IllegalArgumentException
	 */
	public void build() throws RejectedExecutionException, InvalidTokenException, IllegalArgumentException{
		this.jda = JDABuilder.createLight(TOKEN, Collections.emptyList())
			      .addEventListeners(new SlashCommandListener()/*, new ChannelEditListener()*/)
			      .build();
		this.commands = jda.updateCommands();
		// Add all your commands on this action instance
		commands.addCommands(
				Commands.slash("say", "Makes the bot say what you tell it to")
				.addOption(OptionType.STRING, "content", "What the bot should say", true), // Accepting a user input
				Commands.slash("check", "Check if the bot is working properly") // test add command
//				Commands.slash("leave", "Makes the bot leave the server")
//				.setGuildOnly(true) // this doesn't make sense in DMs
//				.setDefaultPermissions(DefaultMemberPermissions.DISABLED) // only admins should be able to use this command.
				);	
		// Then finally send your commands to discord using the API
		commands.queue();
	}
	
	/**
	 * 加入channel
	 * @param channelId
	 */
	public void addChannel(String channelId) {
		MessageChannel channel = jda.getTextChannelById(channelId);
		if(channel != null) {
			channelMap.put(channelId, channel);
		}
	}
	
	/**
	 * 對單一channel發送訊息
	 * @param message
	 * @param channelId
	 */
	public void sendMessageToChannel(String message, String channelId) {
		if(!channelMap.containsKey(channelId)) {
			return;
		}
		MessageChannel channel = channelMap.get(channelId);
		channel.sendMessage(message).queue();
	}
	
	/**
	 * 對多組channel發送訊息
	 */
	public void sendMessageToChannels(String message, String... channelIdList) {
		if(channelIdList.length == 0) {
			return;
		}
		MessageChannel channel;
		for(String channelId : channelIdList) {
			if(!channelMap.containsKey(channelId)) {
				continue;
			}
			channel = channelMap.get(channelId);
			channel.sendMessage(message).queue();
		}
	}
	
	/**
	 * 對全部channel發送訊息
	 * @param message
	 */
	public void sendMessageToAllChannel(String message) {
		channelMap.forEach((channelId, channel) -> {
			channel.sendMessage(message).queue();
		});
	}
	
	/**
	 * Bot指令監聽器 </br>
	 * 監聽自定義之"/ slash指令"，可依需求增加與修改
	 */
	public class SlashCommandListener extends ListenerAdapter{
		@Override
		public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
			switch (event.getName()) {
			case "say" -> {
				String content = event.getOption("content", OptionMapping::getAsString);
				event.reply(content).queue();
			}
			case "check" -> {
				String channel = event.getChannelId();
				User user = event.getUser();
				String message = "Your channel ID is "+channel
						+",\nthen I have a message for you~ \n"
						+ user.getName() +" your feet is Stinky!!! >W<";
				event.reply(message).queue();
			}
			case "leave" -> {
				event.reply("I'm leaving the server now!")
				.setEphemeral(true) // this message is only visible to the command user
				.flatMap(m -> event.getGuild().leave()) // append a follow-up action using flatMap
				.queue(); // enqueue both actions to run in sequence (send message -> leave guild)
			}
			default -> {return;}
			}
			
		}
	}
	
	/**
	 * 頻道監聽器 </br>
	 * 監聽channel建立與刪除，可搭配資料庫做channel管理
	 * 
	 */
	public class ChannelEditListener extends ListenerAdapter{
		@Override
		public void onChannelCreate(ChannelCreateEvent event) {
			switch(event.getChannelType()) {
			case TEXT -> {
				MessageChannel channel = (MessageChannel) event.getChannel();
				String channelId = channel.getId();
				System.out.println("channelId <"+channelId+ "> created !!!");
				channelMap.put(channelId, channel);
			}
			default -> {return;}
			}
		}
		
		public void onChannelDelete(ChannelDeleteEvent event) {
			switch(event.getChannelType()) {
			case TEXT -> {
				String channelId = event.getChannel().getId();
				System.out.println("channelId <"+channelId+ "> deteled !!!");
				channelMap.remove(channelId);
			}
			default -> {return;}
			}
		}
	}
}
