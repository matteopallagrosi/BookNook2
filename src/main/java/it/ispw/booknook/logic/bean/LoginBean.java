package it.ispw.booknook.logic.bean;

import it.ispw.booknook.logic.Observer;
import it.ispw.booknook.logic.entity.User;
import it.ispw.booknook.logic.entity.UserType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginBean extends Observer {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Image profileImage;
    private ImageView avatar;
    private String address;
    private String city;
    private String zip;
    private String country;
    private boolean isReader;

    public LoginBean() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginBean(String email, String password) {
        this.email = email;
        this.password = password;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (checkEmail(email)) {
            this.email = email;
        }
        else {
            this.email = null;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (checkPassword(password)) {
            this.password = password;
        }
        else {
            this.password = null;
        }
    }

    private boolean checkEmail(String email) { //controllo sintattico email
        EmailValidator validator = EmailValidator.getInstance();
        return (!email.isEmpty() && validator.isValid(email));
    }

    private boolean checkPassword(String password) {  //controllo sintattico password
        		/*(                   # inizio
		(?=.*\d)              #   deve contenere almeno un numero da 0 a 9
		(?=.*[a-z])           #   deve contenere almeno un carattere minuscolo
		(?=.*[A-Z])           #   deve contenere almeno un carattere maiuscolo
		(?=.*[._-])          #   deve contenere almeno un carattere speciale tra questi "!?._-"
		.                     #   deve superare TUTTI i test precedenti
		{6,20}                #   la lunghezza deve essere minimo di 8 caratteri e massimo di 20
		)                     # fine
         */

        //controllo sintattico della password
        String stringPattern = "((?=.*\\d)(?=.*[!?._-]).{8,20})";
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
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

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
            this.profileImage = new Image(profileImage);

    }

    public void setImage(Image image) {
        this.profileImage = image;
    }

    public String getImageUrl() {
        return profileImage.getUrl();
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatarImage(ImageView avatar) {
        this.avatar = avatar;
        //il bean si registra come observer dell'istanza di User
        //in modo da aggiornare l'imageView quando lo User cambia stato (cambia immagine profilo)
        User.getUser().attach(this);
    }

    @Override
    public void update() {
        //recupera la nuovaImmagineProfilo
        String newImageUrl = User.getUser().getImageProfile();
        Image newImageProfile = new Image(newImageUrl);
        avatar.setImage(newImageProfile);
    }

    public UserType getType() {
        if (isReader) return UserType.READER;
        else return UserType.LIBRARIAN;
    }

    public void setIsReader(boolean state) {
        this.isReader = state;
    }


}
