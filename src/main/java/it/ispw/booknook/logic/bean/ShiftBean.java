package it.ispw.booknook.logic.bean;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ShiftBean {
    String usernameLibrary;
    Date date;
    String startTime;
    String endTime;

    public String getUsernameLibrary() {
        return usernameLibrary;
    }

    public void setUsernameLibrary(String usernameLibrary) {
        this.usernameLibrary = usernameLibrary;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = Date.valueOf(date);
    }

    public void setDateString(String date) {
        this.date = Date.valueOf(date);
    }

    public void setStartTime(Time start) {
        this.startTime = start.toString().substring(0, 5);
    }

    public void setEndTime(Time end) {
        this.endTime = end.toString().substring(0, 5);
    }


    //conversione della stringa in Time
    public Time getStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Time t = null;
        try {
            long ms = sdf.parse(this.startTime).getTime();
            t = new Time(ms);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    public Time getEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Time t = null;
        try {
            long ms = sdf.parse(this.endTime).getTime();
            t = new Time(ms);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    public String getTime() {
        return this.startTime + " - " + this.endTime;
    }

    public void setConsultationDate(Date date) {
        this.date = date;
    }
}
