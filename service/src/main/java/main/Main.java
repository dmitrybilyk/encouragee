package main;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime truncatedTimeUtc = time.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime truncatedTimeEurope = truncatedTimeUtc.withZoneSameInstant(ZoneId.of("Australia/Sydney"));
        ZonedDateTime truncatedTime = truncatedTimeUtc.withZoneSameInstant(ZoneId.of("Europe/Kiev"));
//        LocalDateTime truncatedTimeUtcNoZone = truncatedTimeUtc.toLocalDateTime();

        System.out.println(time);
        System.out.println(truncatedTimeUtc);
        System.out.println(truncatedTimeEurope);
        System.out.println(truncatedTime);
//        System.out.println(truncatedTimeUtcNoZone);
    }
}
