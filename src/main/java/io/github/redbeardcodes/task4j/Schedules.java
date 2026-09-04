package io.github.redbeardcodes.task4j;

import java.time.DayOfWeek;
import java.util.BitSet;

public final class Schedules {

	private Schedules() {}

	public static Schedule daily(int hour, int minute) {
		checkHour(hour);
		checkMinute(minute);
		return new Schedule(single(minute), single(hour), all(1, 7), all(1, 31), all(1, 12));
	}

	public static Schedule weekly(DayOfWeek day, int hour, int minute) {
		checkHour(hour);
		checkMinute(minute);
		return new Schedule(single(minute), single(hour), single(day.getValue()), all(1, 31), all(1, 12));
	}

	public static Schedule weekdays(int hour, int minute) {
		checkHour(hour);
		checkMinute(minute);
		BitSet weekdays = new BitSet();
		weekdays.set(DayOfWeek.MONDAY.getValue());
		weekdays.set(DayOfWeek.TUESDAY.getValue());
		weekdays.set(DayOfWeek.WEDNESDAY.getValue());
		weekdays.set(DayOfWeek.THURSDAY.getValue());
		weekdays.set(DayOfWeek.FRIDAY.getValue());

		return new Schedule(single(minute), single(hour), weekdays, all(1, 31), all(1, 12));
	}

	public static Schedule weekends(int hour, int minute) {
		checkHour(hour);
		checkMinute(minute);
		BitSet weekends = new BitSet();
		weekends.set(DayOfWeek.SATURDAY.getValue());
		weekends.set(DayOfWeek.SUNDAY.getValue());

		return new Schedule(single(minute), single(hour), weekends, all(1, 31), all(1, 12));
	}

	public static Schedule monthly(int day, int hour, int minute) {
		if (day < 1 || day > 31)
			throw new IllegalArgumentException("Day must be between 1 and 31: " + day);

		checkHour(hour);
		checkMinute(minute);

		return new Schedule(single(minute), single(hour), all(1, 7), single(day), all(1, 12));
	}

	public static Schedule everyMinute(int interval) {
		if (interval < 1 || interval > 59)
			throw new IllegalArgumentException("Interval must be between 0 and 59: " + interval);

		return new Schedule(every(0, 59, interval), all(0, 23), all(1, 7), all(1, 31), all(1, 12));
	}

	private static BitSet single(int value) {
		BitSet bits = new BitSet();
		bits.set(value);
		return bits;
	}

	private static BitSet all(int first, int last) {
		BitSet bits = new BitSet();
		bits.set(first, last  + 1);
		return bits;
	}

	private static BitSet every(int first, int last, int interval) {
		BitSet bits = new BitSet();
		for (int value = first; value <= last; value += interval)
			bits.set(value);
		return bits;
	}

	private static void checkHour(int hour) {
		if (hour < 0 || hour > 23)
			throw new IllegalArgumentException("Hour must be between 0 and 23: " + hour);
	}

	private static void checkMinute(int minute) {
		if (minute < 0 || minute > 59)
			throw new IllegalArgumentException("Hour must be between 0 and 59: " + minute);
	}
}
