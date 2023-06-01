package com.personal.devetblogapi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateTimeUtil {

  public static Date stringToDate(String dateString) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    return formatter.parse(dateString);
  }

  public static LocalDateTime toLocalDateTime(Date dateToConvert) {
    return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static String minuteToTimeString(Long minute) {
    long hour = minute / 60;
    long newMinute = minute % 60;
    return String.format("%s:%s:00", hour, newMinute);
  }
}
