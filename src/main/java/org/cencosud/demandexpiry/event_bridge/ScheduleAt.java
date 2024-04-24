package org.cencosud.demandexpiry.event_bridge;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleAt {

    private static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String AT = "at";
    private static final String OPEN = "(";
    private static final String CLOSE = ")";

    private final ZonedDateTime instant;

    public ScheduleAt(ZonedDateTime instant) {
        this.instant = instant;
    }

    public String expression() {
        String dateString = instant.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS));
        return AT + OPEN + dateString + CLOSE;
    }

    public ZoneId timeZone() {
        return instant.getZone();
    }

}
