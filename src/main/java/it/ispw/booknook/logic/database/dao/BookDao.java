package it.ispw.booknook.logic.database.dao;

import it.ispw.booknook.logic.database.BookNookDB;
import it.ispw.booknook.logic.database.queries.BookQueries;
import it.ispw.booknook.logic.entity.Book;
import it.ispw.booknook.logic.entity.BookCopy;
import it.ispw.booknook.logic.entity.Library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDao {

    private BookDao() {}

    public static List<Book>  getRequestedBooks(String title) {
        List<Book> list = new ArrayList<Book>();
        Connection conn = null;
        Book book;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getBooks(conn, title);

            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }
           book = new Book(rs.getString("titolo"), rs.getString("autore"));
           list.add(book);

            //altrimenti libri presenti
            while (rs.next()) {
                book = new Book(rs.getString("titolo"), rs.getString("autore"));
                list.add(book);
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static Book getSelectedBookDetails(String title, String author) {
        Book book = new Book();
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getABook(conn, title, author);

            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }

            book.setIsbn(rs.getString("ISBN"));
            book.setTitle(rs.getString("titolo"));
            book.setAuthor(rs.getString("autore"));
            book.setPublisher(rs.getString("editore"));
            book.setPublishingYear(rs.getInt("anno"));

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //recupera i tag associati a quel libro
        try {
            ResultSet rs = BookQueries.getTags(conn, book.getIsbn());

            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }

            book.setTag(rs.getString("descrizione"));

            while (rs.next()) {
                book.setTag(rs.getString("descrizione"));
            }

            rs.close();
        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }

    public static List<String> getTagsByISBN(String ISBN) {
        //recupera i tag associati a quel libro
        Connection conn = null;
        List<String> tags = new ArrayList<>();

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            ResultSet rs = BookQueries.getTags(conn, ISBN);


            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }

            tags.add(rs.getString("descrizione"));

            while (rs.next()) {
                tags.add(rs.getString("descrizione"));
            }

            rs.close();
        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }

    public static List<Book> getRelatedBooks(List<String> tags, String isbn){
        List<Book> bookList = new ArrayList<>();
        Connection conn = null;
        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            for (int i= 0; i< tags.size(); i++) {
                ResultSet rs = BookQueries.getBooksByTags(conn, isbn, tags.get(i));
                while(rs.next()){
                    Book book = new Book();
                    book.setIsbn(rs.getString("libri.ISBN"));
                    book.setTitle(rs.getString("titolo"));
                    book.setAuthor(rs.getString("autore"));
                    book.setPublisher(rs.getString("editore"));
                    book.setPublishingYear(rs.getInt("anno"));
                    book.setTag(tags.get(i));
                    bookList.add(book);
                }
                rs.close();
            }

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public static void insertFavorites(String reader, String listName, String isbn) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            BookQueries.insertBookInList(conn, reader, listName, isbn);

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Book>  getFavoriteBooks(String username, String listName) {
        List<Book> list = new ArrayList<Book>();
        Connection conn = null;
        Book book;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getList(conn, username, listName);

            if (!rs.first()){ // rs empty
                throw new Exception("No List found matching with name");
            }

            rs.first();
            book = new Book();
            book.setIsbn(rs.getString("libri.ISBN"));
            System.out.println(rs.getString("libri.ISBN"));
            book.setTitle(rs.getString("titolo"));
            book.setAuthor(rs.getString("autore"));
            list.add(book);

            //altrimenti libri presenti
            while (rs.next()) {
                book = new Book();
                book.setIsbn(rs.getString("libri.ISBN"));
                System.out.println(rs.getString("libri.ISBN"));
                book.setTitle(rs.getString("titolo"));
                book.setAuthor(rs.getString("autore"));
                list.add(book);
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //recupera le informazioni sui libri attualmente in prestito dell'utente
    public static List<BookCopy>  getBooksOnLoan(String username) {
        List<BookCopy> list = new ArrayList<BookCopy>();
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getLoanBooks(conn, username);

            if (!rs.first()){ // rs empty
                throw new Exception("No List found matching with username");
            }

            rs.first();
            Book book = new Book();
            book.setIsbn(rs.getString("libri.ISBN"));
            book.setTitle(rs.getString("titolo"));
            book.setAuthor(rs.getString("autore"));
            BookCopy copy = new BookCopy();
            copy.setLoanDate(rs.getDate("data_prestito"));
            copy.setBook(book);
            Library library = new Library();
            library.setName(rs.getString("biblioteche.nome")); //biblioteca da cui è in prestito la copia
            copy.setLibrary(library);
            list.add(copy); //lista di copie in prestito al lettore

            //altrimenti libri presenti
            while (rs.next()) {
                book = new Book();
                book.setIsbn(rs.getString("libri.ISBN"));
                book.setTitle(rs.getString("titolo"));
                book.setAuthor(rs.getString("autore"));
                copy = new BookCopy();
                copy.setLoanDate(rs.getDate("data_prestito"));
                copy.setBook(book);
                library = new Library();
                library.setName(rs.getString("biblioteche.nome")); //biblioteca da cui è in prestito la copia
                copy.setLibrary(library);
                list.add(copy); //lista di copie in prestito al lettore
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean getListByISBN(String username, String isbn) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        boolean state = false;
        try {
            ResultSet rs = BookQueries.getBookInList(conn, username, isbn);

            state = rs.first();

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return state;
    }

    public static void deleteBookFromList(String username, String isbn) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            BookQueries.removeBookFromList(conn, username, isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
