package fi.bioklaani.klaanonbot.tasks;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/** Describes a task executed in some abstract manner. Tasks should not maintain
* internal data and therefore can be invoked multiple times.
* {@param A} the type of the argument this task requires
* {@param R} the type of data this task returns*/
public interface BotTask<A, R> {

	/** Runs this task. A thrown {@code BotException} indicates
	* that the task failed to run.*/
	public R run(A arg);
	
	/** Returns the {@code RunPolicy} that describes whether this
	* task should be retried.*/
	public default RunPolicy retry() {
		return RunPolicy.no();
	}
	
	/** Returns the {@code RunPolicy} that describes the execution
	* of this task when executed after being retrieved from the
	* {@code then()} tasks of another task. Note that to repeat
	* itself, a task can return a {@code Supplier} of itself from
	* {@code then()}. The default value, a {@code RunPolicy} that
	* states that the task cannot be run, indicates that it is not
	* allowed to chain this task.*/
	public default RunPolicy chained() {
		return RunPolicy.no();
	}
	
	/** Returns a {@code List} of tasks that should be ran
	* after this task completes succesfully.*/
	public default List<Supplier<BotTask<R, ?>>> then() {
		return Collections.emptyList();
	}
	
	/** Returns a string that describes this task. */
	public default String getName() {
		return getClass().getSimpleName();
	}
}
