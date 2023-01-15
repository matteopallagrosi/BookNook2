package it.ispw.booknook.logic.database.queries;

import java.sql.*;

public class LibraryQueries {

    private LibraryQueries() {}

    public static ResultSet getLibraries(Connection connection, String isbn) throws SQLException {
        String query = "SELECT * FROM biblioteche JOIN copie ON biblioteca = username where ISBN LIKE ?";
        PreparedStatement pstmt = connection.prepareStatement( query );
        pstmt.setString( 1, isbn);
        return pstmt.executeQuery();
    }

    public static int setBookOnLoan(Connection connection, String usernameUtente, Date prestitoData, String isbn, String usernameBiblioteca, int id) throws SQLException {
        String query = "UPDATE copie SET stato = 0, prestito_lettore = ?, data_prestito = ? WHERE ISBN = ? AND biblioteca = ? AND id = ?";
        PreparedStatement pstmt = connection.prepareStatement( query );
        pstmt.setString( 1, usernameUtente);
        pstmt.setDate( 2, prestitoData);
        pstmt.setString( 3, isbn);
        pstmt.setString( 4, usernameBiblioteca);
        pstmt.setInt( 5, id);
        return pstmt.executeUpdate();
    }


}
