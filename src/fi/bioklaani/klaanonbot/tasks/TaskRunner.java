package fi.bioklaani.klaanonbot.tasks;

import java.lang.InterruptedException;
import java.util.Deque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.time.temporal.ChronoUnit;

import fi.bioklaani.klaanonbot.BotException;
import fi.bioklaani.klaanonbot.Logging;

/** Runs and manages {@code BotTask}s.*/
public class TaskRunner {

	private ExecutorService executor = Executors.newWorkStealingPool();
	private Deque<Future<?>> runningTasks = new ConcurrentLinkedDeque<>();
	
	public <R> void waitTask(BotTask<Void, R> task) {
		run(task, null);
		while(true) {
			try {
				runningTasks.remove().get();
				if(runningTasks.isEmpty()) { break; }
			} catch(Throwable t) {}
		}
		Logging.logInfo("No more tasks");
	}
	
	private <A, R> Future<R> run(BotTask<A, R> task, A arg) {
		return run(task, arg, 0);
	}
	
	private <A, R> Future<R> run(BotTask<A, R> task, A arg, long waitMs) {
		Future<R> future = executor.submit(() -> {
			boolean failure = false;
			try {
				Thread.sleep(waitMs);
				
				Logging.logInfo("Running " + task.getName());
				R result = task.run(arg);
				
				runNextTasks(task, result);
				
				return result;
			} catch(BotException e) {
				failure = true;
				Logging.logBotException(e, task);
			} catch(Throwable t) {
				Logging.logError(t, task);
			} finally {
				if(failure && task.retry().shouldRun()) {
					run(task, arg, task.retry()
							.duration().toMillis());
				}
			}
			
			throw new BotException("This should never be shown.");
		});
		
		runningTasks.add(future);
		return future;
	}
	
	private <A, R> void runNextTasks(BotTask<A, R> task, R result) {
		for(Supplier<BotTask<R, ?>> supplier : task.then()) {
			BotTask<R, ?> nextTask = supplier.get();
			
			if(nextTask.chained().shouldRun()) {
				if(task.chained().shouldRun()) {
					run(nextTask, result, nextTask.chained()
							.duration().toMillis());
				} else {
					run(nextTask, result);
				}
			} else {
				Logging.logInfo("Tried to chain " + nextTask.getName()
					+ " from " + task.getName() + " though not allowed.");
			}
		}
	}
}
