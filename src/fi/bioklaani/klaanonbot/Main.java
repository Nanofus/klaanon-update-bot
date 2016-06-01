package fi.bioklaani.klaanonbot;

import fi.bioklaani.klaanonbot.tasks.TaskRunner;
import fi.bioklaani.klaanonbot.tasks.InitializeTask;

public class Main {
	public static void main(String[] args) {
		TaskRunner runner = new TaskRunner();
		
		Logging.logInfo("Started program");
		runner.waitTask(new InitializeTask());
	}
}
