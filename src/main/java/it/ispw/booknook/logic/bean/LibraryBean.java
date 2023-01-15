package it.ispw.booknook.logic.bean;

import it.ispw.booknook.logic.entity.Library;

public class LibraryBean {
    private String username;
    private String name;
    private String address;
    private String openingTime;
    private String closingTime;
    private double latitude;
    private double longitude;
    private String city;
    private boolean availability;
    private String isbnAvailableBook;
    private int idCopyAvailable;

    public LibraryBean(Library library) {
        this.setUsername(library.getUsername());
        this.setName(library.getName());
        this.setAddress(library.getAddress());
        this.setOpeningTime(library.getOpeningTime().toString().substring(0, 5));
        this.setClosingTime(library.getClosingTime().toString().substring(0, 5));
        this.setLatitude(library.getLatitude().doubleValue());
        this.setLongitude(library.getLongitude().doubleValue());
        this.setCity(library.getCity());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getIsbnAvailableBook() {
        return isbnAvailableBook;
    }

    public void setIsbnAvailableBook(String isbnAvailableBook) {
        this.isbnAvailableBook = isbnAvailableBook;
    }

    public int getIdCopyAvailable() {
        return idCopyAvailable;
    }

    public void setIdCopyAvailable(int idCopyAvailable) {
        this.idCopyAvailable = idCopyAvailable;
    }
}
