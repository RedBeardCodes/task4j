package io.github.redbeardcodes.task4j;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class SchedulerTest {
	@Test
	public void longRunningTaskDoesNotBlockScheduler() throws Exception {
		var firstStarted = new CountDownLatch(1);
		var releaseFirst = new CountDownLatch(1);
		var secondStarted = new CountDownLatch(1);

		var scheduler = new Scheduler(ZoneOffset.UTC);

		scheduler.schedule(Schedules.everyMinute(1), () -> {
			firstStarted.countDown();
			try {
				releaseFirst.await();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		});

		scheduler.schedule(Schedules.everyMinute(2), secondStarted::countDown);

		scheduler.tick(ZonedDateTime.of(2026, 9, 1, 15, 0, 0, 0, ZoneOffset.UTC));

		assertTrue(firstStarted.await(1, TimeUnit.SECONDS), "First started failed");
		assertTrue(secondStarted.await(1, TimeUnit.SECONDS), "Second started failed");

		releaseFirst.countDown();

		scheduler.close();
	}

	@Test
	public void taskIsNotDispatchedWhenScheduleDoesNotMatch() throws Exception {
		var executed = new CountDownLatch(1);
		var scheduler = new Scheduler(ZoneOffset.UTC);
		var now = ZonedDateTime.of(2026, 9, 1, 15, 0, 0, 0, ZoneOffset.UTC);

		scheduler.schedule(Schedules.daily(16, 0), executed::countDown);
		scheduler.tick(now);
		scheduler.close();

		assertFalse(executed.await(100, TimeUnit.MILLISECONDS));
	}

}
