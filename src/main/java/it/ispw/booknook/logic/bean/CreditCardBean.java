package it.ispw.booknook.logic.bean;

import it.ispw.booknook.logic.boundary.CardValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditCardBean {
    String number;
    String code;
    String ownerName;
    Date expiryDate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        //validazione sintattica del numero di carta
        if (CardValidator.validitychek(Long.parseLong(number)))
            this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getExpiryDate() {
        return expiryDate.toString();
    }

    public void setExpiryDate(String expiryDate) {
        try {
            this.expiryDate = new SimpleDateFormat("MM/yy").parse(expiryDate);
            if (this.expiryDate.before(new Date())) {
                this.expiryDate = null;
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }
    }
}
