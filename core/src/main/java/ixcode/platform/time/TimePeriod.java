package ixcode.platform.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static Date tomorrow() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
    }

    public static Date yesterday() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
    }

    public static Date previousDayFrom(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static Date nextDayFrom(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
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