package it.ispw.booknook.logic.bean;

import it.ispw.booknook.logic.entity.Rate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewBean {
    private String username;  //username dell'utente che ha recensito
    private String title;
    private String text;
    private int serviceRate;
    private int availabilityRate;
    private int locationRate;
    private String date;
    private int mediumRate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(int serviceRate) {
        this.serviceRate = serviceRate;
    }

    public int getAvailabilityRate() {
        return availabilityRate;
    }

    public void setAvailabilityRate(int availabilityRate) {
        this.availabilityRate = availabilityRate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLocationRate() {
        return locationRate;
    }

    public void setLocationRate(int locationRate) {
        this.locationRate = locationRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        this.date = strDate;
    }

    public int getMediumRate() {
        return mediumRate;
    }

    public void setMediumRate(int mediumRate) {
        this.mediumRate = mediumRate;
    }

    public void setRate(Rate rate) {
        this.serviceRate = rate.getService();
        this.availabilityRate = rate.getAvailability();
        this.locationRate = rate.getLocation();
    }

    public void setAvailability(String availability){
        this.availabilityRate = Integer.valueOf(availability);
    }
    public void setService(String service){
        this.serviceRate = Integer.valueOf(service);
    }
    public void setLocation(String location){
        this.locationRate = Integer.valueOf(location);
    }


}
