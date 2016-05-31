package fi.bioklaani.klaanonbot.tasks;

import java.time.Duration;

/** Describes the possible execution of a {@code BotTask},
* possible after some amount of time has passed.*/
public class RunPolicy {

	private final static RunPolicy DO_NOT_RUN = new RunPolicy();

	private final Duration duration;
	
	private RunPolicy(Duration duration) {
		this.duration = duration;
	}
	
	private RunPolicy() {
		this.duration = null;
	}
	
	public boolean shouldRun() { return duration != null; }
	
	public Duration duration() { return duration; }
	
	public static RunPolicy no() {
		return DO_NOT_RUN;
	}
	
	public static RunPolicy in(Duration duration) {
		return new RunPolicy(duration);
	}
}
