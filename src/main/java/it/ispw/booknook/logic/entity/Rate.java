package it.ispw.booknook.logic.entity;

import java.io.Serializable;

public class Rate implements Serializable {
    private int service;
    private int availability;
    private int location;

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    //ritorna il numero medio di stelle assegnato in una certa recensione
    public int getMediumRate() {
        int sum = getService() + getAvailability() + getLocation();
        return sum/3;
    }
}
