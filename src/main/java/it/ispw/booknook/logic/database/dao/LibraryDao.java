package it.ispw.booknook.logic.database.dao;

import it.ispw.booknook.logic.database.BookNookDB;
import it.ispw.booknook.logic.database.queries.LibraryQueries;
import it.ispw.booknook.logic.entity.Book;
import it.ispw.booknook.logic.entity.BookCopy;
import it.ispw.booknook.logic.entity.Library;
import it.ispw.booknook.logic.entity.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryDao {

    private LibraryDao(){}

    public static Map<String, Library> getLibrariesByISBN(String isbn){
        HashMap<String, Library> libraries = new HashMap<>();
        Connection conn = null;
        Book book = new Book(isbn);


        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = LibraryQueries.getLibraries(conn, isbn);

            while(rs.next()){ // rs empty
            String name = rs.getString("nome");
            Library library = libraries.get(name);
            if (library == null) {
                library = new Library();
                library.setUsername(rs.getString("username"));
                library.setName(rs.getString("nome"));
                library.setAddress(rs.getString("indirizzo"));
                library.setOpeningTime(rs.getTime("ora_apertura"));
                library.setClosingTime(rs.getTime("ora_chiusura"));
                library.setLatitude(rs.getBigDecimal("latitudine"));
                library.setLongitude(rs.getBigDecimal("longitudine"));
                library.setCity(rs.getString("città"));
                libraries.put(name, library);
                library.addBook(book);
            }

            BookCopy copy = new BookCopy();
            copy.setId(rs.getInt("id"));
            copy.setBook(book);
            copy.setLibrary(library);
            copy.setState(rs.getInt("stato"));
            book.addCopy(copy);
            library.addCopy(copy);
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return libraries;
    }

    public static void setCopyOnLoan(BookCopy copy) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        String username = User.getUser().getUsername();
        long millis = System.currentTimeMillis();
        Date currentDate = new Date(millis);
        String isbn = copy.getBook().getIsbn();
        String biblioteca = copy.getLibrary().getUsername();
        int id = copy.getId();
        try {
            LibraryQueries.setBookOnLoan(conn, username, currentDate, isbn, biblioteca, id);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
