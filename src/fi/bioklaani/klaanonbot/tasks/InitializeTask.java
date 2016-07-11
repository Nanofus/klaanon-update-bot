package fi.bioklaani.klaanonbot.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import fi.bioklaani.klaanonbot.storage.PostCache;
import fi.bioklaani.klaanonbot.Bot;

/** Initializes everything.*/
public class InitializeTask implements BotTask<Void, Void> {

	private final static List<Supplier<BotTask<Void, ?>>> THEN =
			Arrays.asList(ReadSiteTask::new);

	public Void run(Void ignored) {
		PostCache.initialize();
		Bot.initialize();
		return null;
	}
	
	public List<Supplier<BotTask<Void, ?>>> then() {
		return THEN;
	}
}
