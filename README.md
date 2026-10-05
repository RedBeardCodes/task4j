# Task4J

Task4J is a small Java task scheduler for applications that need to execute tasks on a schedule without introducing a scheduling framework or job-management system.

## Goals

* Simple scheduling without extensive frameworks
* No job management in the core engine
* Small API
* No external dependencies
* Virtual-thread-based task execution
* Typed Java schedules rather than a cron expression language

## Usage

Create a `Scheduler`, register tasks with a `Schedule`, and start it.

```java
var scheduler = new Scheduler(ZoneId.systemDefault());

scheduler.schedule(
        Schedules.daily(2, 30),
        this::performCleanup);

scheduler.schedule(
        Schedules.weekly(DayOfWeek.MONDAY, 15, 0),
        this::generateReport);

scheduler.schedule(
        Schedules.everyMinutes(15),
        this::poll);

scheduler.start();
```

`Scheduler` implements `AutoCloseable`, so it can also be used with try-with-resources:

```java
try (var scheduler = new Scheduler(ZoneId.systemDefault())) {
    scheduler.schedule(
            Schedules.daily(2, 30),
            this::performCleanup);

    scheduler.start();

    // Application continues running...
}
```

Tasks are represented by `Runnable` instances. Task execution is independent of the scheduler loop: when a task is due, Task4J starts it on a virtual thread.

## Schedules

Schedules are created using the typed Java API provided by `Schedules`.

### Daily

```java
Schedules.daily(15, 0);
```

Runs every day at 15:00.

### Weekly

```java
Schedules.weekly(DayOfWeek.MONDAY, 15, 0);
```

Runs every Monday at 15:00.

### Weekdays

```java
Schedules.weekdays(8, 30);
```

Runs Monday through Friday at 08:30.

### Weekends

```java
Schedules.weekends(10, 0);
```

Runs Saturday and Sunday at 10:00.

### Monthly

```java
Schedules.monthly(1, 0, 0);
```

Runs on the first day of every month at midnight.

If a monthly schedule specifies a day that does not exist in a particular month, it simply does not run that month. For example:

```java
Schedules.monthly(31, 12, 0);
```

does not run in February, April, June, September, or November.

### Intervals

```java
Schedules.everyMinutes(15);
```

Runs every 15 minutes.

## Architecture

Task4J deliberately separates scheduling from task execution.

```text
Schedule
    │
    │ matches current time?
    ▼
Scheduler
    │
    │ task is due
    ▼
virtual thread
    │
    ▼
Runnable
```

A `Schedule` describes when a task should run. Internally, schedules use `BitSet` instances to represent valid minutes, hours, weekdays, month days, and months.

The scheduler periodically evaluates registered schedules against the current time. When a schedule matches, the associated `Runnable` is dispatched on a virtual thread.

The scheduler does not wait for a task to finish before evaluating other tasks.

This means a long-running or blocking task does not occupy the scheduler itself or prevent other scheduled tasks from being dispatched.

## Why BitSets?

Task4J does not implement the cron expression language.

A schedule only needs to answer:

> Does this schedule match this point in time?

BitSets provide a simple representation for that question.

For example, a schedule for Monday at 15:00 can represent:

```text
minute   → 00
hour     → 15
weekday  → Monday
monthday → any
month    → any
```

Matching is consequently a small set of membership checks rather than a cron-expression parser or calendar-rule engine.

## Why Virtual Threads?

Task4J uses virtual threads for task execution.

Scheduled tasks commonly perform blocking operations such as database access, HTTP requests, file I/O, or other application-level work. Virtual threads allow those tasks to execute without requiring a large pool of platform threads.

Task4J does not attempt to manage task concurrency, worker pools, retries, or task queues. The application remains responsible for the behavior of its tasks.

## What Task4J Is Not

Task4J intentionally does not attempt to be:

* A job-management framework
* A workflow engine
* A persistent job queue
* A distributed scheduler
* A retry framework
* A task history or monitoring system
* A cron implementation

Task4J also does not attempt to encode application-specific business rules into schedules.

For example, if an application needs to run something on the third Monday of a month unless that day is a holiday, the scheduler can run the task every Monday and the application can decide whether the work should actually be performed.

The scheduler determines **when to invoke a task**. The application determines **what the task should do**.

## Project Status

This project is in early development.

The API and behavior may change as the project develops.

## License

Copyright 2026 Jeff Rogers [redbeardcodes@gmail.com](mailto:redbeardcodes@gmail.com)

Licensed under the Apache License, Version 2.0.

