package it.ispw.booknook.logic.database.queries;

import it.ispw.booknook.logic.entity.User;

import java.math.BigDecimal;
import java.sql.*;

public class LogQueries {

    private LogQueries() {
    }

    public static int saveReaderUser(Connection connection, User user, String userType) throws SQLException {
        String query = "INSERT INTO utenti (username, email, password, tipo) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getPassword());
        pstmt.setString(4, userType);
        return pstmt.executeUpdate();  // ritorna il numero di righe inserite/aggiornate
    }

    public static int saveUsername(Connection connection, User user) throws SQLException {
        String query = "INSERT INTO lettori (username, immagine_profilo) VALUES (?, 0)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, user.getUsername());

        return pstmt.executeUpdate();
    }

    public static ResultSet selectReaderUser(Connection connection, String email) throws SQLException {
        String query = "SELECT * FROM utenti where email = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, email);
        return pstmt.executeQuery();
    }

    public static ResultSet getReaderImage(Connection connection, String username) throws SQLException {
        String query = "SELECT immagine_profilo FROM lettori where username = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        return pstmt.executeQuery();
    }


    public static ResultSet getpass(Connection connection, String email) throws SQLException {
        String query = "SELECT password FROM utenti where email = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, email);
        return pstmt.executeQuery();
    }

    public static int saveEmail(Connection connection, String oldEmail, String newEmail) throws SQLException {
        String query = "UPDATE utenti SET email = ? WHERE email = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, newEmail);
        pstmt.setString(2, oldEmail);
        return pstmt.executeUpdate();
    }

    public static int savePassword(Connection connection, String username, String password) throws SQLException {
        String query = "UPDATE utenti SET password = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, password);
        pstmt.setString(2, username);
        return pstmt.executeUpdate();
    }

    public static int updateProfileDetails(Connection connection, String username, String firstName, String lastName, String address, String city, String zip, String country) throws SQLException {
        String query = "UPDATE lettori SET nome = ?, cognome = ?, indirizzo = ?, città = ?, codice_postale = ?, paese = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, address);
        pstmt.setString(4, city);
        pstmt.setString(5, zip);
        pstmt.setString(6, country);
        pstmt.setString(7, username);
        return pstmt.executeUpdate();
    }

    public static ResultSet getProfileDetails(Connection connection, String username) throws SQLException {
        String query = "SELECT nome, cognome, indirizzo, città, codice_postale, paese FROM lettori WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        return pstmt.executeQuery();
    }

    public static int saveImage(Connection connection, String username, Integer numImage) throws SQLException {
        String query = "UPDATE lettori SET immagine_profilo = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, numImage);
        pstmt.setString(2, username);
        return pstmt.executeUpdate();
    }

    public static int deleteReader(Connection connection, String username) throws SQLException {
        String query = "DELETE FROM lettori WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        return pstmt.executeUpdate();
    }

    public static int deleteUser(Connection connection, String username) throws SQLException {
        String query = "DELETE FROM utenti WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        return pstmt.executeUpdate();
    }

    public static int insertLibraryDetails(Connection connection, String username, String name, String address, String city, Time start, Time end) throws SQLException {
        String query = "INSERT INTO biblioteche (username, nome, indirizzo, ora_apertura, ora_chiusura, latitudine, longitudine, città) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, name);
        pstmt.setString(3, address);
        pstmt.setTime(4, start);
        pstmt.setTime(5, end);
        //TODO
        pstmt.setBigDecimal(6, BigDecimal.valueOf(48.675463));
        pstmt.setBigDecimal(7, BigDecimal.valueOf(48.675463));
        pstmt.setString(8, city);
        return pstmt.executeUpdate();
    }
}