package it.ispw.booknook.logic;

public interface Encrypter {

    //verifica corrispondeza tra stringa e hash
    boolean verify(String insertedString, String hash);

    //converte stringa in sequenza cifrata
    String encrypt(String stringToHash);
}
