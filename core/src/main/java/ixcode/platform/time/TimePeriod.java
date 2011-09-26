package ixcode.platform.time;

import java.util.*;

import static ixcode.platform.time.TimeDuration.lastingFor;

public class TimePeriod {
    private Date startDate;
    private TimeDuration duration;



    public static TimePeriod forATimePeriodOf(int duration, TimeUnit timeUnit, Date startDate) {
        return new TimePeriod(startDate, lastingFor(duration, timeUnit));
    }

    public static Date fromToday() {
        return new Date();
    }

    public static Date today() {
        return new Date();
    }

    public static Date forToday() {
        return new Date();
    }

    private TimePeriod(Date startDate, TimeDuration duration) {
        this.startDate = startDate;
        this.duration = duration;
    }

    public boolean contains(Date date) {
        return true;
    }

    public static Date now() {
        return new Date();
    }


    public static class TimePeriodBuilder {

        private Date startDate;

        public TimePeriodBuilder(Date startDate) {
            this.startDate = startDate;
        }

        public static TimePeriodBuilder startingOn(Date startDate) {
            return new TimePeriodBuilder(startDate);
        }


        public TimePeriod lastingFor(TimeDuration duration) {
            return new TimePeriod(startDate, duration);
        }
    }

}