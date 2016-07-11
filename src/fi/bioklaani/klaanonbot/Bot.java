package fi.bioklaani.klaanonbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import fi.bioklaani.klaanonbot.BotException;
import fi.bioklaani.klaanonbot.Utils;

import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.response.*;
import com.pengrad.telegrambot.request.*;

/** Represents the Telegram bot.*/
public class Bot {

	private final static Path BOT_CONFIG = FileSystems.getDefault().getPath("bot_config.json");

	public static Bot INSTANCE = null;
	
	private String token;
	private List<Long> chatIds;
	private transient TelegramBot tgBot;
	private int offset;
	
	public Bot(String token, List<Long> chatIds, int offset) {
		this.token = token;
		this.chatIds = chatIds;
		this.offset = offset;
		tgBot = TelegramBotAdapter.build(token);
	}
	
	/** Initializes the bot.*/
	public static void initialize() {
		try(BufferedReader br = Files.newBufferedReader(BOT_CONFIG,
				StandardCharsets.UTF_8)) {
			INSTANCE = Utils.ofJSON(Utils.readBufferedReader(br), Bot.class);
		} catch(IOException e) {
			throw new BotException(e, "Failed to read bot config");
		} finally {}
		INSTANCE.tgBot = TelegramBotAdapter.build(INSTANCE.token);
	}
	
	/** Sends a message to the chat specified in bot_config.json.*/
	public void send(String msg) {
		for(long id : chatIds) {
			tgBot.execute(new SendMessage(id, msg));
			Logging.logInfo("Sent update to " + id);
		}
	}
	
	private void send(String msg, long id) {
		tgBot.execute(new SendMessage(id, msg));
	}
	
	/** Updates chats to which this bot sends messages.*/
	public void updateChats() {
		GetUpdatesResponse response = tgBot.execute(new GetUpdates().offset(offset+1));
		List<Update> updates = response.updates();
		if(updates == null || updates.isEmpty()) {
			Logging.logInfo("No TG updates");
			return;
		}
		
		for(Update update : updates) {
			if(update.message().text() != null) {
				String msg = update.message().text();
				long id = update.message().chat().id();
				if(msg.equals("/add")) {
					if(!chatIds.contains(id)) {
						chatIds.add(id);
						send("You are now subscribed.", id);
					} else {
						send("You are already subscribed.", id);
					}
					writeConfig();
					Logging.logInfo("Added chat " + id);
				} else if(msg.equals("/remove")) {
					if(chatIds.contains(id)) {
						chatIds.remove(id);
						send("You are no longer subscribed.", id);
					} else {
						send("You are not subscribed.", id);
					}
					writeConfig();
					Logging.logInfo("Removed chat " + id);
				}
			}
			offset = update.updateId() > offset ? update.updateId() : offset;
		}
		
		writeConfig();
	}
	
	private void writeConfig() {
		try(BufferedWriter br = Files.newBufferedWriter(BOT_CONFIG,
				StandardCharsets.UTF_8)) {
			br.write(Utils.toJSON(this));
		} catch(IOException e) {
			throw new BotException(e, "Failed to write bot config");
		} finally {}
	}
}
