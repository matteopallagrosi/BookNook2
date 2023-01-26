package it.ispw.booknook.logic.database.queries;

import java.sql.*;

public class BookQueries {

    private BookQueries() {}

    public static ResultSet getBooks(Connection connection, String title) throws SQLException {
        String query = "SELECT titolo,autore FROM libri where (titolo LIKE ? or autore LIKE ?) AND consultabile = ?";
        PreparedStatement pstmt = connection.prepareStatement( query );
        pstmt.setString( 1, "%" + title + "%");
        pstmt.setString( 2, "%" + title + "%");
        pstmt.setInt(3,0);
        return pstmt.executeQuery();
    }

    //ritorna le informazioni di un possibile libro con un titolo e autore
    public static ResultSet getABook(Connection connection, String title, String author) throws SQLException {
        String query = "SELECT * FROM libri where titolo LIKE ? and autore LIKE ? LIMIT 1";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, "%" + title + "%");
        pstmt.setString( 2, "%" + author + "%");
        return pstmt.executeQuery();
    }

    //recupera tutti i tag di un certo libro
    public static ResultSet getTags(Connection connection, String isbn) throws SQLException {
        String query = "SELECT descrizione FROM libri JOIN tag_libri ON libri.ISBN = tag_libri.ISBN JOIN tag ON tag_libri.tag = tag.id  where libri.ISBN LIKE ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, isbn);
        return pstmt.executeQuery();
    }

    //recupera i libri che possiedono quei tag
    public static ResultSet getBooksByTags(Connection connection, String isbn, String tag) throws SQLException {
        String query = "SELECT * FROM libri JOIN tag_libri ON libri.ISBN = tag_libri.ISBN JOIN tag on tag_libri.tag = tag.id WHERE descrizione = ? AND libri.ISBN <> ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, tag);
        pstmt.setString(2, isbn);
        return pstmt.executeQuery();
    }

    public static int insertBookInList(Connection connection, String reader, String listName, String isbn) throws SQLException {
        String query = "INSERT INTO liste_lettura (lettore, nome, ISBN) VALUES (?, ?,?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, reader);
        pstmt.setString(2, listName);
        pstmt.setString(3, isbn);
        return pstmt.executeUpdate();  //ritorna il numero di righe inserite
    }
    //per le liste want to read e books i liked
    public static ResultSet getList(Connection connection, String username, String listName) throws SQLException{
        String query= "SELECT * FROM liste_lettura JOIN libri ON liste_lettura.ISBN = libri.ISBN WHERE lettore LIKE ? AND nome LIKE ? ";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, username);
        pstmt.setString(2, listName);
        return pstmt.executeQuery();
    }


    //per la lista books on Loan
    public static ResultSet getLoanBooks(Connection connection, String username) throws SQLException{
        String query= "SELECT * FROM liste_lettura JOIN libri ON liste_lettura.ISBN = libri.ISBN JOIN copie ON libri.ISBN = copie.ISBN JOIN biblioteche ON copie.biblioteca = biblioteche.username WHERE prestito_lettore LIKE ? AND lettore LIKE ? AND liste_lettura.nome LIKE 'Books on loan'";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, username);
        pstmt.setString( 2, username);
        return pstmt.executeQuery();
    }

    public static ResultSet getBookInList(Connection connection, String username, String isbn) throws SQLException{
        String query= "SELECT * FROM liste_lettura WHERE lettore LIKE ? AND ISBN LIKE ? AND nome <> 'Books on loan'";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, username);
        pstmt.setString( 2, isbn);
        return pstmt.executeQuery();
    }

    public static int removeBookFromList(Connection connection, String username, String isbn) throws SQLException{
        String query= "DELETE FROM liste_lettura WHERE lettore = ? AND ISBN = ? AND nome <> 'Books on loan'";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, username);
        pstmt.setString( 2, isbn);
        return pstmt.executeUpdate();
    }

    public static ResultSet getBooksByGenre(Connection connection, String tag) throws SQLException {
        String query = "SELECT * FROM tag_libri JOIN tag ON tag_libri.tag = tag.id JOIN libri ON libri.ISBN = tag_libri.ISBN WHERE tag.descrizione = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, tag);
        return pstmt.executeQuery();
    }


    public static ResultSet getConsultationBooks(Connection connection, String title) throws SQLException {
        String query = "SELECT * FROM libri where (titolo LIKE ? or autore LIKE ?) AND consultabile = 1";
        PreparedStatement pstmt = connection.prepareStatement( query );
        pstmt.setString( 1, "%" + title + "%");
        pstmt.setString( 2, "%" + title + "%");
        return pstmt.executeQuery();
    }

    public static int updateConsultation(Connection connection, String username, String library, Date consDate, Time start) throws SQLException {
        String query = "UPDATE turni_consultazione SET lettore_prenotato = ? WHERE biblioteca LIKE ? AND data = ? AND ora_inizio = ?";
        PreparedStatement pstmt = connection.prepareStatement( query );
        pstmt.setString( 1, username);
        pstmt.setString( 2, library);
        pstmt.setDate( 3, consDate);
        pstmt.setTime( 4, start);
        return pstmt.executeUpdate();
    }

    public static int insertBook(Connection connection, String isbn, String title, String author, String publisher, int year, int consultation) throws SQLException {
        String query = "INSERT INTO libri (ISBN, titolo, autore, editore, anno, consultabile) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, isbn);
        pstmt.setString(2,title);
        pstmt.setString(3, author);
        pstmt.setString(4, publisher);
        pstmt.setInt(5, year);
        pstmt.setInt(6, consultation);
        return pstmt.executeUpdate();
    }

    public static int insertCopy(Connection connection, String isbn, String library) throws SQLException {
        String query = "INSERT INTO copie (ISBN, biblioteca, stato) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, isbn);
        pstmt.setString(2,library);
        pstmt.setInt(3, 1);
        return pstmt.executeUpdate();
    }

    public static ResultSet getAllTags(Connection connection) throws SQLException {
        String query = "SELECT descrizione FROM tag";
        PreparedStatement pstmt = connection.prepareStatement( query );
        return pstmt.executeQuery();
    }

    //assegna un nuovo tag ad un libro
    public static int setTag(Connection connection, String isbn, int idTag) throws SQLException {
        String query = "INSERT INTO tag_libri (ISBN, tag) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString( 1, isbn);
        pstmt.setInt(2, idTag);
        return pstmt.executeUpdate();
    }

    public static ResultSet getIdTag(Connection connection, String description) throws SQLException {
        String query = "SELECT id FROM tag WHERE descrizione LIKE ?";
        PreparedStatement pstmt = connection.prepareStatement( query );
        pstmt.setString(1, description);
        return pstmt.executeQuery();
    }


}
