package io.github.redbeardcodes.task4j;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Scheduler implements AutoCloseable {

	private final Clock clock;
	private final List<ScheduledTask> tasks;
	private final Thread schedulerThread;

	private volatile boolean running;

	public Scheduler(ZoneId zone) {
		this(Clock.system(zone));
	}

	public Scheduler(Clock clock) {
		this.clock = clock;
		this.tasks = new CopyOnWriteArrayList<>();
		this.schedulerThread = Thread.ofVirtual()
			.name("scheduler")
			.unstarted(this::run);
	}

	public void schedule(Schedule schedule, Runnable task) {
		tasks.add(new ScheduledTask(schedule, task));
	}

	private void run() {
		while (running) {
			ZonedDateTime now = ZonedDateTime.now(clock)
				.withSecond(0)
				.withNano(0);

			tick(now);
			sleepUntilNextMinute(now);
		}
	}

	void tick(ZonedDateTime now) {
		for(ScheduledTask task : tasks) {
			if (task.schedule().matches(now)) {
				Thread.startVirtualThread(task.task());
			}
		}
	}

	private void sleepUntilNextMinute(ZonedDateTime now) {
		ZonedDateTime next = now.plusMinutes(1);
		long millis = Duration.between(ZonedDateTime.now(clock), next).toMillis();
		if (millis <= 0)
			return;

		try {
			Thread.sleep(millis);
		} catch (InterruptedException ie) {
			if (running)
				Thread.currentThread().interrupt();
		}
	}

	@Override
	public void close() throws Exception {
		running = false;
		schedulerThread.interrupt();
	}
}
