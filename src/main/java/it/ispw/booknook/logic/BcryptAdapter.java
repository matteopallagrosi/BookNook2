package it.ispw.booknook.logic;

import at.favre.lib.crypto.bcrypt.BCrypt;
import it.ispw.booknook.logic.Encrypter;

public class BcryptAdapter implements Encrypter {

    BCrypt.Verifyer verifyer;
    BCrypt.Hasher encrypter;

     public BcryptAdapter() {
         verifyer = BCrypt.verifyer();  //inizializza il riferimento alle classi da adattare
         encrypter = BCrypt.withDefaults();
     }

    @Override
    public boolean verify(String insertedString, String hash) {
         //converto la stringa in un'array di char (compatibile con interfaccia di Bcrypt)
        char[] charArray = insertedString.toCharArray();
        //inoltra la richiesta alla classe da adattare
        BCrypt.Result result = verifyer.verify(charArray, hash);
        return result.verified; //ritorna vero se stringa e hash corrispondono
    }

    @Override
    public String encrypt(String stringToHash) {
         char[] charArray = stringToHash.toCharArray();
         return encrypter.hashToString(12, charArray);
    }
}
