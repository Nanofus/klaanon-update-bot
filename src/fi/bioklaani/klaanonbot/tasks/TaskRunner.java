package fi.bioklaani.klaanonbot.tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.time.temporal.ChronoUnit;

import fi.bioklaani.klaanonbot.BotException;

/** Runs and manages {@code BotTask}s.*/
public class TaskRunner {

	private ExecutorService executor = Executors.newWorkStealingPool();
	
	public <R> void waitTask(BotTask<Void, R> task) {
		try {
			run(task, null).get();
		} catch(InterruptedException | ExecutionException e) {
			throw new BotException(e, "Interrupted while waiting main task");
		}
	}
	
	private <A, R> Future<R> run(BotTask<A, R> task, A arg) {
		return run(task, arg, 0);
	}
	
	private <A, R> Future<R> run(BotTask<A, R> task, A arg, long waitMs) {
		return executor.submit(() -> {
			try {
				Thread.sleep(waitMs);
				
				R result = task.run(arg);
				runNextTasks(task, result);
				
				if(task.repeat().shouldRun()) {
					result = run(task, arg, task.repeat()
							.duration().get(ChronoUnit.MILLIS)).get();
					runNextTasks(task, result);
				}
				
				return result;
			} catch(BotException e) {
				if(task.retry().shouldRun()) {
					run(task, arg, task.retry()
							.duration().get(ChronoUnit.MILLIS));
				}
			} catch(Throwable t) {
				t.printStackTrace(System.err);
				System.exit(1);
			}
			
			throw new BotException("Failed task " + task.getClass().getSimpleName());
		});
	}
	
	private <A, R> void runNextTasks(BotTask<A, R> task, R result) {
		for(Supplier<BotTask<R, ?>> supplier : task.then()) {
			run(supplier.get(), result);
		}
	}
	
	private void log(BotException e) {
		e.printStackTrace(System.err);
	}
}
