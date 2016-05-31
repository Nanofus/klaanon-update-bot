package fi.bioklaani.klaanonbot.tasks;

import fi.bioklaani.klaanonbot.storage.PostCache;

/** Initializes everything.*/
public class InitializeTask implements BotTask<Void, Void> {

	public Void run(Void ignored) {
		PostCache.initialize();
		return null;
	}
}
