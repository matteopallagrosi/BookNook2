package it.ispw.booknook.logic.bean;

import it.ispw.booknook.logic.boundary.CardValidator;
import it.ispw.booknook.logic.exception.FormatException;
import it.ispw.booknook.logic.exception.InvalidDateException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditCardBean {
    String number;
    String code;
    String ownerName;
    Date expiryDate;

    public CreditCardBean(String number, String code, String owner, String date) throws InvalidDateException, FormatException {
        setNumber(number);
        setCode(code);
        setOwnerName(owner);
        setExpiryDate(date);
    }

    public CreditCardBean(){}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) throws FormatException {
        //validazione sintattica del numero di carta
        if (CardValidator.validitychek(Long.parseLong(number)))
            this.number = number;
        else throw new FormatException("Invalid card number");
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

    public void setExpiryDate(String expiryDate) throws FormatException, InvalidDateException {
        try {
            this.expiryDate = new SimpleDateFormat("MM/yy").parse(expiryDate);
        } catch(ParseException e) {
            throw new FormatException("Invalid expiry date format", e);
        }
        if (this.expiryDate.before(new Date())) {
            throw new InvalidDateException("Credit card expired");
        }
    }

    public Date getDate() {
        return this.expiryDate;
    }
}
