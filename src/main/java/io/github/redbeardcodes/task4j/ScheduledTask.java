package io.github.redbeardcodes.task4j;

import java.util.Objects;

public record ScheduledTask(Schedule schedule, Runnable task) {
	public ScheduledTask {
		Objects.requireNonNull(schedule, "schedule");
		Objects.requireNonNull(task, "task");
	}
}
