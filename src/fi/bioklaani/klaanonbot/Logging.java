package fi.bioklaani.klaanonbot;

import fi.bioklaani.klaanonbot.BotException;
import fi.bioklaani.klaanonbot.tasks.BotTask;

/** Responsible for writing logs based on various settings.*/
public class Logging {

	public static void logError(Throwable e, BotTask<?, ?> task) {
		if(e instanceof BotException) {
			logBotException((BotException) e, task);
		}
		System.out.println("[FATAL ERROR:" + time() + "] " + e.getClass().getSimpleName()
			+ ": " + e.getMessage() + " in " + task.getName());
		e.printStackTrace();
		System.exit(1);
	}
	
	public static void logBotException(BotException e, BotTask<?, ?> task) {
		System.out.println("[ERROR:" + time() + "] " + e.getMessage()  + " in " + task.getName());
	}
	
	public static void logInfo(String msg) {
		System.out.println("[INFO:" + time() + "] " + msg);
	}
	
	private static String time() {
		return "" + System.currentTimeMillis();
	}
}
