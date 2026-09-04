package io.github.redbeardcodes.task4j;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

public class ScheduleTest {

	@Test
	public void dailyScheduleMatchesCorrectTime() {
		var schedule = Schedules.daily(15, 0);

		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 1, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 2, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 3, 16, 0, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void weeklyScheduleMatchesCorrectTime() {
		var schedule = Schedules.weekly(DayOfWeek.MONDAY, 15, 0);

		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 7, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 7, 14, 59, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 14, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 14, 13, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 15, 15, 0, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void weekdayScheduleMatchesCorrectTime() {
		var schedule = Schedules.weekdays(0, 15);

		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 3, 0, 15, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 4, 0, 15, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 5, 0, 15, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 6, 0, 15, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void weekendsScheduleMatchesCorrectTime() {
		var schedule = Schedules.weekends(0, 15);

		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 3, 0, 15, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 4, 0, 15, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 5, 0, 15, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 6, 0, 15, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void monthlyScheduleMatchesCorrectTime() {
		var schedule = Schedules.monthly(10, 15, 0);

		assertFalse(schedule.matches(ZonedDateTime.of(2026, 1, 9, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 1, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 2, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 3, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 4, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 5, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 6, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 7, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 8, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 9, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 10, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 11, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 12, 10, 15, 0, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void dailyAcceptsBoundaryTimes() {
		assertDoesNotThrow(() -> Schedules.daily(0, 0));
		assertDoesNotThrow(() -> Schedules.daily(23, 59));
	}

	@Test
	public void dailyRejectsInvalidHour() {
		assertThrows(IllegalArgumentException.class, () -> Schedules.daily(24, 0));
	}

	@Test
	public void dailyRejectsInvalidMinute() {
		assertThrows(IllegalArgumentException.class, () -> Schedules.daily(0, 60));
	}

	@Test
	public void monthlyThirtyFirstMatchesMonthsWithThirtyOneDays() {
		var schedule = Schedules.monthly(31, 12, 0);
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 1, 31, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 3, 31, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 5, 31, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 7, 31, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 8, 31, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 10, 31, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertTrue(schedule.matches(ZonedDateTime.of(2026, 12, 31, 12, 0, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void monthlyThirtyFirstDoesNotMatchMonthsWithoutThirtyOneDays() {
		var schedule = Schedules.monthly(31, 12, 0);
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 2, 28, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 4, 30, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 6, 30, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 9, 30, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 11, 30, 12, 0, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void monthlyTwentyNinthMatchesFebruaryLeapYear() {
		var schedule = Schedules.monthly(29, 12, 0);
		assertTrue(schedule.matches(ZonedDateTime.of(2028, 2, 29, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertFalse(schedule.matches(ZonedDateTime.of(2026, 2, 28, 12, 0, 0, 0, ZoneOffset.UTC)));
		assertThrows(DateTimeException.class, () -> schedule.matches(ZonedDateTime.of(2026, 2, 29, 12, 0, 0, 0, ZoneOffset.UTC)));
	}

	@Test
	public void everyFifteenMinutesMatchesExpectedMinutes() {
		var schedule = Schedules.everyMinute(15);
		var date = LocalDate.of(2026, 9, 1);

		assertTrue(schedule.matches(date.atTime(10, 0).atZone(ZoneOffset.UTC)));
		assertTrue(schedule.matches(date.atTime(10, 15).atZone(ZoneOffset.UTC)));
		assertTrue(schedule.matches(date.atTime(10, 30).atZone(ZoneOffset.UTC)));
		assertTrue(schedule.matches(date.atTime(10, 45).atZone(ZoneOffset.UTC)));
		assertFalse(schedule.matches(date.atTime(10, 1).atZone(ZoneOffset.UTC)));
	}

}
