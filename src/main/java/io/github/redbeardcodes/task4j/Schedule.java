package io.github.redbeardcodes.task4j;

import java.time.ZonedDateTime;
import java.util.BitSet;
import java.util.Objects;

public final class Schedule {

	private final BitSet minutes;
	private final BitSet hours;
	private final BitSet weekdays;
	private final BitSet monthDays;
	private final BitSet months;

	Schedule(BitSet minutes, BitSet hours, BitSet weekdays, BitSet monthDays, BitSet months) {
		this.minutes = copy(minutes);
		this.hours = copy(hours);
		this.weekdays = copy(weekdays);
		this.monthDays = copy(monthDays);
		this.months = copy(months);
	}

	public boolean matches(ZonedDateTime time) {
		Objects.requireNonNull(time, "time");
		return minutes.get(time.getMinute()) &&
			hours.get(time.getHour()) &&
			weekdays.get(time.getDayOfWeek().getValue()) &&
			monthDays.get(time.getDayOfMonth()) &&
			months.get(time.getMonthValue());
	}

	private static BitSet copy(BitSet source) {
		return (BitSet) source.clone();
	}
}
