package it.ispw.booknook.logic.entity;


import it.ispw.booknook.logic.Subject;

//Singleton
public class User extends Subject {

    private static User user = null;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserType type;
    private String imageProfile;
    private String address;
    private String city;
    private String zip;
    private String country;

    private User() {}

    public static User getUser() {
        if (user == null)
            User.user = new User();
        return user;
    }



    public void setLogDetails(String username, String email, String password,UserType type) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setType(type);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public static boolean isLogged() {
        return user != null;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
        //notifica del cambiamento/inizializzazione immagine profilo
        notifyObservers();
    }

    public static void deleteUser() {
       user = null;
    }
}
