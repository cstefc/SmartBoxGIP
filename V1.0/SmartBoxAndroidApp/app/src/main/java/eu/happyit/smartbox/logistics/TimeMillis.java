package eu.happyit.smartbox.logistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class TimeMillis {
    private long timeInMillis;
    private String timeString;
    private ArrayList<Integer> longMonths = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));
    private ArrayList<Integer> shortMonths = new ArrayList<>(Arrays.asList(4, 6, 9, 11));

    public TimeMillis(int hours, int minutes){
        getMillisAlarm(hours, minutes);
    }

    // Returning the time in milliseconds
    public long getTimeInMillis (){
        return timeInMillis;
    }

    public String getTimeString (){
        return timeString;
    }

    private void getMillisAlarm (int hours, int minutes) {
        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();

        if ((hours<hour) || (hours == hour && minutes<minute)) {
            if (longMonths.contains(month)){
                // Starting a new year
                if (month == 12){
                    if (day == 31) {
                        year++;
                        month = 1;
                        day = 1;
                    }
                    else{
                        day++;
                    }
                }
                // Normal long months
                if (day == 31) {
                    month= month+1;
                    day = 1;
                }
                else{
                    day++;
                }
            }
            else if(shortMonths.contains(month)){
                // Normal short months
                if (day == 30) {
                    month= month+1;
                    day = 1;
                }
                else{
                    day++;
                }
            }
            else if (month == 2){
                // Checking if it's a leap year
                if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)){
                    if (day == 29){
                        month++;
                        day = 1;
                    }else{
                        day++;
                    }
                }
                // If it's a normal year
                else if (day == 28) {
                    month= month+1;
                    day = 1;
                }
                else{
                    day++;
                }
            }
        }

        if (hours >= 10 && minutes >= 10){
            timeString = String.format("%s-%s-%s %s:%s",day, month, year, hours, minutes);
        }
        else if (hours < 10 && minutes <10){
            timeString = String.format("%s-%s-%s 0%s:0%s",day, month, year, hours, minutes);
        }
        else if (hours < 10){
            timeString = String.format("%s-%s-%s 0%s:%s",day, month, year, hours, minutes);
        }
        else if (minutes < 10){
            timeString = String.format("%s-%s-%s %s:0%s",day, month, year, hours, minutes);
        }
        try {
            Date date = new SimpleDateFormat("dd-M-yyyy HH:mm", Locale.getDefault()).parse(timeString);
            timeInMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
