package it.ispw.booknook.logic.entity;


import java.sql.Date;
import java.sql.Time;

public class ConsultationShift {
    Date date;
    Time startTime;
    Time endTime;
    User user;

    public ConsultationShift(Date date, Time start, Time end) {
        setDate(date);
        setStartTime(start);
        setEndTime(end);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
