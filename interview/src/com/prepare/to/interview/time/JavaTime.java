package com.prepare.to.interview.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JavaTime {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime);

//        ZoneId zoneId = ZoneId.of("Europe/Kiev");
        ZoneId zoneId = ZoneId.of("Europe/Berlin");
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now(zoneId);
        System.out.println(zonedDateTime1);

        ZoneId destZoneId = ZoneId.of("Europe/Kiev");
        ZonedDateTime destZonedDateTime = zonedDateTime1.withZoneSameInstant(destZoneId);
        System.out.println(destZonedDateTime);

        ZoneOffset offset = ZoneOffset.of("+03:00");
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, offset);
        System.out.println(offsetDateTime);



        LocalDate initialDate = LocalDate.parse("2007-05-10");
//        LocalDate finalDate = initialDate.plus(Period.ofDays(5));
        LocalDate finalDate = initialDate.plus(5, ChronoUnit.DAYS);
//        LocalDate finalDate = initialDate.plusDays(5, ChronoUnit.DAYS);
        System.out.println(finalDate);
//        int five = Period.between(initialDate, finalDate).getDays();
        long five = ChronoUnit.DAYS.between(initialDate, finalDate);
        System.out.println(five);


        LocalTime initialTime = LocalTime.of(6, 30, 0);
        LocalTime finalTime = initialTime.plus(Duration.ofSeconds(30));
        long thirty = Duration.between(initialTime, finalTime).getSeconds();
//        long thirty = ChronoUnit.SECONDS.between(initialTime, finalTime);
        System.out.println(thirty);


        LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), ZoneId.systemDefault());


        LocalDateTime.ofEpochSecond(1465817690, 0, ZoneOffset.UTC);


        LocalDateTime localDateTime3 = LocalDateTime.of(2015, Month.JANUARY, 25, 6, 30);
        String localDateString = localDateTime3.format(DateTimeFormatter.ISO_DATE);
        System.out.println(localDateString);

        System.out.println(localDateTime3
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        .withLocale(Locale.CHINESE)));

    }
}
